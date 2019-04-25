package com.mzc.db;

import com.mzc.db.cfgLabel.LabelEnum;
import com.mzc.db.config.EmfConfig;
import com.mzc.db.mysql.MysqlEntityManager;
import com.mzc.db.testEntity.Player;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class DatabaseEntityMgrFactory {

    private static volatile DatabaseEntityMgrFactory inst = null;

    public static final String CREATE_TABLE_TAIL = " ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";

    private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";

    private static final String cfgProperty = "simpleDbCfg";

    public static final String BASE_SEQUENCE_TABLE = "SEQUENCE";
    /**
     * 管理定时存库线程
     */
    public ScheduledExecutorService exexutor = null;

    private String dbUrl = null;
    private Properties dbProperties = null;

    private int serverId;
    private String serverName;

    private Driver d = null;

    private HashMap<String, MysqlEntityManager> entityMap = new HashMap<>();

    private DatabaseEntityMgrFactory() {
    }

    public static DatabaseEntityMgrFactory getInst() {
        if (inst == null) {
            synchronized (DatabaseEntityMgrFactory.class) {
                if (inst == null) {
                    inst = new DatabaseEntityMgrFactory();
                    try {
                        inst.init();
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return inst;
    }

    private void init() throws Exception {
        String cfgPath = System.getProperty(cfgProperty);
        if (cfgPath == null) {
            throw new Exception("需要指定配置文件路径, propertyKey: " + cfgProperty);
        }
        EmfConfig config = this.parseCfg(cfgPath);
        {
            this.serverId = config.getServerId();
            this.serverName = config.getServerName();
            this.dbUrl = config.getDbUrl();
            this.dbProperties = config.getDbProperties();
            int entitySize = config.getEntityConfigs().size();
            int avaiableProcessor = Runtime.getRuntime().availableProcessors();
            int coreSize = entitySize > avaiableProcessor ? avaiableProcessor : entitySize;
            this.exexutor = Executors.newScheduledThreadPool(coreSize);
        }
        this.d = (Driver) Class.forName(MYSQL_DRIVER).newInstance();
        this.checkBaseTable();
        for (EmfConfig.EntityConfig cfg : config.getEntityConfigs()) {
            DbEntityInfo entityInfo = new DbEntityInfo(cfg.id, cfg.classPath, null);
            MysqlEntityManager entityManager = new MysqlEntityManager(entityInfo);
            entityManager.init();
            entityMap.put(entityInfo.getClassName(), entityManager);
        }
    }

    /**
     * 获取数据库链接
     *
     * @return
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        // TODO: 2019/4/16 需要调整为连接池管理
        return d.connect(this.dbUrl, this.dbProperties);
    }

    /**
     * 检查基础sequence表是否存在
     */
    public void checkBaseTable() throws SQLException {
        try (Connection conn = this.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            try (ResultSet rs = metaData.getTables(null, null, BASE_SEQUENCE_TABLE, null);) {
                if (!rs.next()) {           //表不存在，需要创建基础sequeence表
                    String sql = "CREATE TABLE " + BASE_SEQUENCE_TABLE + "(id int not null primary key auto_increment, classname varchar(200) not null, curId bigint not null, key idx_classname (classname))" + CREATE_TABLE_TAIL;
                    try (Statement statement = conn.createStatement()) {
                        statement.executeUpdate(sql);
                    }
                }
            }
            System.out.println("初始化 " + BASE_SEQUENCE_TABLE + " 完成");
        }

    }

    private EmfConfig parseCfg(String cfgPath) throws Exception {
        EmfConfig config = new EmfConfig();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream is = Files.newInputStream(Paths.get(cfgPath));
        Document document = builder.parse(is);
        Element root = document.getDocumentElement();
        for (LabelEnum le : LabelEnum.values()) {
            NodeList nodeList = root.getElementsByTagName(le.getLabelName());
            le.parse(nodeList.item(0), config);
        }
        return config;
    }

    public MysqlEntityManager getEntityMgr(Class clazz) {
        return entityMap.get(clazz.getName());
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public Properties getDbProperties() {
        return dbProperties;
    }

    public Driver getD() {
        return d;
    }

    public int getServerId() {
        return serverId;
    }

    public String getServerName() {
        return serverName;
    }

    public void destory() {
        exexutor.shutdown();
    }

    public static void main(String[] args) throws Exception {
        System.setProperty(cfgProperty, "D:\\git\\db\\src\\main\\resources\\conf\\simpleEntity.xml");
        DatabaseEntityMgrFactory inst = DatabaseEntityMgrFactory.getInst();
       MysqlEntityManager<Player> mgr = inst.getEntityMgr(Player.class);
        /* List<Integer> list = new ArrayList<>();
        list.add(666);
        Map<Integer, String> map = new HashMap<>();
        map.put(999, "qwer");
        Player p = new Player(mgr.nextId(), "asdf", (byte) 1, true, 333, 11.2f, 334.55d, new int[]{1, 2, 3, 4}, list, map);
        mgr.insert(p, true);
        System.out.println(p.getId());*/
//        Player p = mgr.getEntity(1101000000000022529L);
//        System.out.println(p.getAa3().get(999));
//        System.out.println(mgr.getEntity(1101000000000022529L));

    }
}
