# dbFramework
简单的数据库存储框架，适合简单的单机游戏服务器/应用服务器使用，省去开发人员自己建表/分表，让开发人员更专注于业务逻辑

限制：
  暂时只实现了单服，没有实现高可用，后续有时间可以考虑将数据先同步到redis集群，定时去redis中取出需要存储的数据做持久化

实现：
  1.根据配置的class对象自动生成对应数据表
  2.封装简单的增删改查/生成唯一主键/根据接口查询数据表中部分字段及条件查询等功能
  3.使用ScheduledThreadPoolExecutor线程池管理数据同步线程，允许每张表自定义入库间隔
  4.使用连接池管理mysql连接
  5.支持自动分表功能（分表逻辑为按照单表存储数据量大小）
  6.使用弱引用来缓存数据，因此如果不是立即存储的数据需要上层应用持有引用，否则可能会有数据丢失
  7.在服务器关闭时需要调用destory方法，此方法会尝试保存所有需要入库但暂时没有入库的对象

配置需求：simpleEntity.xml
