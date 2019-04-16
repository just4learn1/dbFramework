package com.mzc.db;

import com.mzc.db.cfgLabel.LabelEnum;
import com.mzc.db.config.EmfConfig;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DatabaseEntityMgr {

    private static volatile DatabaseEntityMgr inst = null;

    private static final String cfgProperty = "simpleDbCfg";

    private DatabaseEntityMgr() {
    }

    public static DatabaseEntityMgr getInst() {
        if (inst == null) {
            synchronized (DatabaseEntityMgr.class) {
                if (inst == null) {
                    inst = new DatabaseEntityMgr();
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
        EmfConfig cfg = this.parseCfg(cfgPath);
        System.out.println(cfg);
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
        DatabaseEntityMgr inst = DatabaseEntityMgr.getInst();

    }
}
