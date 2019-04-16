package com.mzc.db.cfgLabel;

import com.mzc.db.cfgLabel.parser.DbConfParser;
import com.mzc.db.cfgLabel.parser.EntityParser;
import com.mzc.db.cfgLabel.parser.ILabelPaser;
import com.mzc.db.cfgLabel.parser.ServerBaseParser;
import com.mzc.db.config.EmfConfig;
import org.w3c.dom.Node;

public enum LabelEnum {
    SERVER_BASE("base-cfg", new ServerBaseParser()),
    ENTITY("entity-cfg", new EntityParser()),
    DB_PARAMETER("dbParameter", new DbConfParser()),
    ;

    private String labelName;

    private ILabelPaser parser;

    LabelEnum(String labelName, ILabelPaser parser) {
        this.labelName = labelName;
        this.parser = parser;
    }

    public void parse(Node node, EmfConfig config){
        parser.parse(node, config);
    }

    public String getLabelName() {
        return labelName;
    }
}
