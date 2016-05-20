@XmlJavaTypeAdapters({
        @XmlJavaTypeAdapter(value=StringAdapter.class, type=String.class)
})
package org.fao.etl.layer.fenix.impl.decoder.wms.dto;

import org.fao.etl.layer.fenix.impl.decoder.wms.StringAdapter;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;