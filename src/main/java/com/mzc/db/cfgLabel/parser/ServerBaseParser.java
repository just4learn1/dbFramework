package com.mzc.db.cfgLabel.parser;

import com.mzc.db.config.EmfConfig;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Objects;

public class ServerBaseParser implements ILabelPaser {

    @Override
    public void parse(Node node, EmfConfig config) {
        NodeList nodeList = node.getChildNodes();
        String serverId = null, serverName = null;
        for (int i = 0, n=nodeList.getLength(); i < n; i++) {
            Node child = nodeList.item(i);
            String nodeName = child.getNodeName().trim();
            if(nodeName.equalsIgnoreCase("serverId")){
                serverId = child.getTextContent().trim();
            }else if(nodeName.equalsIgnoreCase("serverName")){
                serverName = child.getTextContent().trim();
            }
        }
        try{
            config.setServerId(Integer.parseInt(serverId));
            Objects.requireNonNull(serverName);
            config.setServerName(serverName);
        }catch (RuntimeException e){
            e.printStackTrace();
            throw new RuntimeException("[serverId:"+serverId+"] [serverName:"+serverName+"]");
        }
    }
}
