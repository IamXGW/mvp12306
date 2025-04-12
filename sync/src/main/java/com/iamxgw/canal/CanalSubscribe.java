package com.iamxgw.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry.*;
import com.alibaba.otter.canal.protocol.Message;
import com.iamxgw.service.TrainSeatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author IamXGW
 * @since 2025-04-10
 */
@Service
@Slf4j
public class CanalSubscribe implements ApplicationListener {

    @Resource
    TrainSeatService trainSeatService;

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        canalSubscribe();
    }

    private void canalSubscribe() {
        // 创建链接
        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress("47.100.178.16",
                11111), "base_info", "", "");
        int batchSize = 1000;
        new Thread(() -> {
            try {
                log.info("canal subscribe");
                connector.connect();
                connector.subscribe(".*\\..*");
                connector.rollback();
                while (true) {
                    // 获取指定数量的数据
                    Message message = connector.getWithoutAck(batchSize);
                    long batchId = message.getId();
                    int size = message.getEntries().size();
                    if (batchId == -1 || size == 0) {
                        safeSleep(100);
                        continue;
                    }
                    try {
                        log.info("new message, batchId:{}, size:{}", batchId, size);
                        handleEntry(message.getEntries());
                        // 提交确认
                        connector.ack(batchId);
                    } catch (Exception e1) {
                        log.error("canal data handle exception, batchId:{}", batchId, e1);
                        // 处理失败, 回滚数据
                        connector.rollback(batchId);
                    }
                }
            } catch (Exception e2){
                log.error("canal subscribe exception", e2);
                safeSleep(1000);
                canalSubscribe();
            }
        }).start();
    }

    private void handleEntry(List<Entry> entrys) {
        for (Entry entry : entrys) {
            if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == EntryType.TRANSACTIONEND) {
                continue;
            }

            RowChange rowChange = null;
            try {
                rowChange = RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(),
                        e);
            }

            EventType eventType = rowChange.getEventType();
            String schemaName = entry.getHeader().getSchemaName();
            String tableName = entry.getHeader().getTableName();
            log.info("name:[{},{}],eventType:{}", schemaName, tableName, eventType);

            for (RowData rowData : rowChange.getRowDatasList()) {
                if (EventType.DELETE == eventType) {
                    handleColumn(rowData.getBeforeColumnsList(), eventType, schemaName, tableName);
                } else {
                    handleColumn(rowData.getAfterColumnsList(), eventType, schemaName, tableName);
                }
            }
        }
    }

    private void handleColumn(List<Column> columns, EventType eventType, String schemaName, String tableName) {
        if (schemaName.contains("train_seat")) {
            trainSeatService.handle(columns, eventType);
        } else if ("train_number".equals(schemaName)) {

        } else {
            log.info("drop data, no need to handle");
        }
    }

    private void safeSleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {

        }
    }

}