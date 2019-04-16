package com.mzc.db.cfgLabel.parser;

import com.mzc.db.config.EmfConfig;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class EntityParser implements ILabelPaser {
    @Override
    public void parse(Node node, EmfConfig config) {
        NodeList nodeList = node.getChildNodes();
        for (int i = 0, n=nodeList.getLength(); i < n; i++) {
            Node node1 = nodeList.item(i);
            if(node1.getAttributes() != null){
                NamedNodeMap namedNodeMap = node1.getAttributes();
                config.addEntityConfig(Integer.parseInt(namedNodeMap.getNamedItem("id").getTextContent()), namedNodeMap.getNamedItem("class").getTextContent());
            }
        }
    }
}
