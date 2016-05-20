package org.fao.etl.layer.fenix.impl.decoder.wms.dto;

import javax.xml.bind.annotation.XmlElement;
import java.util.Collection;

public class KeywordList {

    @XmlElement public Collection<String> Keyword;
}
