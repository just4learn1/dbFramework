package com.mzc.db.cfgLabel.parser;

import com.mzc.db.config.EmfConfig;
import org.w3c.dom.Node;

public interface ILabelPaser {
    void parse(Node node, EmfConfig config);
}
