package org.fao.etl.layer.fenix;


import org.fao.fenix.commons.msd.dto.full.MeIdentification;

import java.io.InputStream;
import java.util.Collection;

public interface Decoder {

    public Collection<MeIdentification> decode (InputStream input) throws Exception;
}
