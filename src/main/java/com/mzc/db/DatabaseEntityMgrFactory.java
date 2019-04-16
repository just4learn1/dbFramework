package com.mzc.db;

import com.mzc.db.cfgLabel.LabelEnum;
import com.mzc.db.config.EmfConfig;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;

public class DatabaseEntityMgrFactory {

    private static volatile DatabaseEntityMgrFactory inst = null;

    private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";

    private static final String cfgProperty = "simpleDbCfg";

    private EmfConfig config = null;

    private Driver d = null;

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
        if(cfgPath == null){
            throw new Exception("需要指定配置文件路径, propertyKey: " + cfgProperty);
        }
        this.config = this.parseCfg(cfgPath);
        this.d = (Driver) Class.forName(MYSQL_DRIVER).newInstance();
    }

    /**
     * 获取数据库链接
     * @return
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        // TODO: 2019/4/16 需要调整为连接池管理
        return d.connect(this.config.getDbUrl(), this.config.getDbProperties());
    }

    private EmfConfig parseCfg(String cfgPath) throws Exception {
        EmfConfig config = new EmfConfig();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream is = Files.newInputStream(Paths.get(cfgPath));
        Document document = builder.parse(is);
        Element root = document.getDocumentElement();
        for(LabelEnum le : LabelEnum.values()) {
            NodeList nodeList = root.getElementsByTagName(le.getLabelName());
            le.parse(nodeList.item(0), config);
        }
        return config;
    }

    public static void main(String[] args) {
        System.setProperty(cfgProperty, "D:\\git\\db\\src\\main\\resources\\conf\\simpleEntity.xml");
        DatabaseEntityMgrFactory inst = DatabaseEntityMgrFactory.getInst();
    }
}
