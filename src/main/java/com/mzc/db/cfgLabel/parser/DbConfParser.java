package com.mzc.db.cfgLabel.parser;

import com.mzc.db.config.EmfConfig;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;

public class DbConfParser implements ILabelPaser {
    @Override
    public void parse(Node node, EmfConfig config) {
        NodeList nodeList = node.getChildNodes();
        String dbUrl=null, uasername=null, password=null,connectMaxNum=null,connectTimeOut=null;
        for (int i = 0, n = nodeList.getLength(); i < n; i++) {
            Node node1 = nodeList.item(i);
            String nodeName = node1.getNodeName().trim();
            if (nodeName.equalsIgnoreCase("dburl")) {
                dbUrl = node1.getTextContent().trim();
            } else if (nodeName.equalsIgnoreCase("username")) {
                uasername = node1.getTextContent().trim();
            } else if (nodeName.equalsIgnoreCase("password")) {
                password = node1.getTextContent().trim();
            } else if (nodeName.equalsIgnoreCase("connectMaxNum")) {
                connectMaxNum = node1.getTextContent().trim();
            } else if (nodeName.equalsIgnoreCase("connectTimeOut")) {
                connectTimeOut = node1.getTextContent().trim();
            }
        }
        try{
            config.setDbConfig(dbUrl, uasername, password, connectMaxNum, connectTimeOut);
        } catch (RuntimeException e){
            e.printStackTrace();
            throw new RuntimeException("[dbUrl:"+dbUrl+"] [uasername:"+uasername+"] [password:"+password+"] [connectMaxNum:"+connectMaxNum+"] [connectTimeOut:"+connectTimeOut+"]");
        }
    }
}
