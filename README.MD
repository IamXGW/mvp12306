## 一、整体介绍

该项目是模仿 12306 的 MVP(Minimum Visible Product) 版本

### 1. 项目架构（核心数据流向）![Screenshot 2025-03-16 at 14.57.31](https://cdn.jsdelivr.net/gh/IamXGW/CDN-for-xuguangwei.com@main/uPic/Screenshot%202025-03-16%20at%2014.57.312025%2003%2016%2015%2010%2016)

### 2. 技术栈

![Screenshot 2025-03-16 at 15.13.51](https://cdn.jsdelivr.net/gh/IamXGW/CDN-for-xuguangwei.com@main/uPic/Screenshot%202025-03-16%20at%2015.13.512025%2003%2016%2015%2014%2013)

## 二、需求分析与拆解

#### 2.1 业务实体

城市（TrainCity）、车站（TrainStation）、车次（TrainNumber）、车次详情（TrainNumberDetail）、座位（TrainSeat）、乘客（TrainTraveler）、用户（TrainUser）、订单（TrainOrder）、订单详情（TrainOrderDetail）

#### 2.2 核心流程（绿色标记）

![Screenshot 2025-03-16 at 18.04.44](https://cdn.jsdelivr.net/gh/IamXGW/CDN-for-xuguangwei.com@main/uPic/Screenshot%202025-03-16%20at%2018.04.442025%2003%2016%2018%2004%2051)

#### 2.3 业务介绍

##### 2.3.1 基础数据管理

![Screenshot 2025-03-16 at 18.13.15](https://cdn.jsdelivr.net/gh/IamXGW/CDN-for-xuguangwei.com@main/uPic/Screenshot%202025-03-16%20at%2018.13.152025%2003%2016%2018%2013%2021)

关于座位需要注意以下几点：

1. 不同的车型会有不同的车厢

2. 不用的车厢的座位排列可能不一样

3. 不同的车厢的座位等级可能不一样
4. 每一排多余的空座可能是不一样的

![Screenshot 2025-03-16 at 18.22.33](https://cdn.jsdelivr.net/gh/IamXGW/CDN-for-xuguangwei.com@main/uPic/Screenshot%202025-03-16%20at%2018.22.332025%2003%2016%2018%2022%2041)

![Screenshot 2025-03-16 at 18.28.06](https://cdn.jsdelivr.net/gh/IamXGW/CDN-for-xuguangwei.com@main/uPic/Screenshot%202025-03-16%20at%2018.28.062025%2003%2016%2018%2028%2058)

![Screenshot 2025-03-16 at 18.30.37](https://cdn.jsdelivr.net/gh/IamXGW/CDN-for-xuguangwei.com@main/uPic/Screenshot%202025-03-16%20at%2018.30.372025%2003%2016%2018%2030%2044)

没有提供「修改」功能是因为，如果管理员不小心将车次信息填错，通过人工修改很容易改错，比如需要维护各个站点间的数据。因此，如果当发现车次详情错误的时候，直接通过「删除 - 新增」来代替修改，用户更友好。

![Screenshot 2025-03-16 at 18.31.35](https://cdn.jsdelivr.net/gh/IamXGW/CDN-for-xuguangwei.com@main/uPic/Screenshot%202025-03-16%20at%2018.31.352025%2003%2016%2018%2031%2042)

##### 2.3.2 管理员日常运维

![Screenshot 2025-03-16 at 18.37.50](https://cdn.jsdelivr.net/gh/IamXGW/CDN-for-xuguangwei.com@main/uPic/Screenshot%202025-03-16%20at%2018.37.502025%2003%2016%2018%2037%2058)

![Screenshot 2025-03-20 at 21.20.06](https://cdn.jsdelivr.net/gh/IamXGW/CDN-for-xuguangwei.com@main/uPic/Screenshot%202025-03-20%20at%2021.20.062025%2003%2022%2013%2012%2044)

**一张票的本质：**某个车次的某一段区间（一条线段，包含若干站点）只要区间不重叠，座位就不会发生竞争，可以回收利用（同时预售）

**核心业务规则：**车票包含的每个原子区间的重叠次数不超过总座位数

![Screenshot 2025-03-20 at 21.22.06](https://cdn.jsdelivr.net/gh/IamXGW/CDN-for-xuguangwei.com@main/uPic/Screenshot%202025-03-20%20at%2021.22.062025%2003%2020%2021%2022%2015)

![Screenshot 2025-03-20 at 21.27.18](https://cdn.jsdelivr.net/gh/IamXGW/CDN-for-xuguangwei.com@main/uPic/Screenshot%202025-03-20%20at%2021.27.182025%2003%2020%2021%2027%2026)

##### 2.3.3 用户端维护

![Screenshot 2025-03-20 at 21.30.27](https://cdn.jsdelivr.net/gh/IamXGW/CDN-for-xuguangwei.com@main/uPic/Screenshot%202025-03-20%20at%2021.30.272025%2003%2020%2021%2031%2023)

![Screenshot 2025-03-20 at 21.34.31](https://cdn.jsdelivr.net/gh/IamXGW/CDN-for-xuguangwei.com@main/uPic/Screenshot%202025-03-20%20at%2021.34.312025%2003%2020%2021%2034%2038)

# 三、用户端核心流程处理

![Screenshot 2025-04-07 at 21.50.10](https://cdn.jsdelivr.net/gh/IamXGW/CDN-for-xuguangwei.com@main/uPic/Screenshot%202025-04-07%20at%2021.50.102025%2004%2007%2021%2057%2051)

![Screenshot 2025-04-08 at 19.35.41](https://cdn.jsdelivr.net/gh/IamXGW/CDN-for-xuguangwei.com@main/uPic/Screenshot%202025-04-08%20at%2019.35.412025%2004%2008%2019%2035%2054)
