package com.iamxgw.db;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.shardingsphere.api.algorithm.masterslave.RoundRobinMasterSlaveLoadBalanceAlgorithm;
import io.shardingsphere.api.config.rule.MasterSlaveRuleConfiguration;
import io.shardingsphere.shardingjdbc.api.MasterSlaveDataSourceFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

/**
 * 
 * @author IamXGW
 *@since 2024-07-03 22:21
 */
@Configuration
@MapperScan(basePackages = "com.iamxgw.dao", sqlSessionTemplateRef = "sessionTemplate")
public class BasicDataSourceConfig {

    @Primary
    @Bean(name = DataSources.MASTER_DB)
    @ConfigurationProperties(prefix = "spring.datasource-master")
    public DataSource masterDB() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = DataSources.SLAVE_DB)
    @ConfigurationProperties(prefix = "spring.datasource-slave")
    public DataSource slaveDB() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "masterSlaveDataSource")
    public DataSource masterSlaveDataSource(@Qualifier(DataSources.MASTER_DB) DataSource masterDB,
                                            @Qualifier(DataSources.SLAVE_DB) DataSource slaveDB) throws SQLException {
        Map<String, DataSource> dataSourceMap = Maps.newHashMap();
        dataSourceMap.put(DataSources.MASTER_DB, masterDB);
        dataSourceMap.put(DataSources.SLAVE_DB, slaveDB);

        MasterSlaveRuleConfiguration masterSlaveRuleConfiguration =
                new MasterSlaveRuleConfiguration("ds_master_slave",
                        DataSources.MASTER_DB,
                        Lists.newArrayList(DataSources.SLAVE_DB),
                        new RoundRobinMasterSlaveLoadBalanceAlgorithm());
        return MasterSlaveDataSourceFactory.createDataSource(dataSourceMap,
                masterSlaveRuleConfiguration, Maps.newHashMap(), new Properties());
    }

    @Primary
    @Bean
    public DataSourceTransactionManager transactionManager(@Qualifier(DataSources.MASTER_DB) DataSource masterDB) {
        return new DataSourceTransactionManager(masterDB);
    }

    @Primary
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier(DataSources.MASTER_DB) DataSource masterDB) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(masterDB);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mappers/*.xml"));
        return bean.getObject();
    }

    @Primary
    @Bean(name = "sessionTemplate")
    public SqlSessionTemplate sessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}