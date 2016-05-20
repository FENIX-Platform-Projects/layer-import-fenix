package org.fao.etl.layer.fenix.impl.decoder.wms;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class StringAdapter extends XmlAdapter<String,String> {
    @Override
    public String marshal(String string) throws Exception {
        return string!=null ? string.trim() : "";
    }

    @Override
    public String unmarshal(String string) throws Exception {
        return string!=null && string.trim().length()>0 ? string.trim() : null;
    }

}
