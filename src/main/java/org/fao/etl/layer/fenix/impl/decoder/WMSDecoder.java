package org.fao.etl.layer.fenix.impl.decoder;

import org.fao.etl.layer.fenix.Decoder;
import org.fao.fenix.commons.msd.dto.full.MeIdentification;

import javax.enterprise.context.ApplicationScoped;
import java.io.InputStream;
import java.util.Collection;

@ApplicationScoped
public class WMSDecoder implements Decoder {

    @Override
    public Collection<MeIdentification> decode(InputStream input) throws Exception {
        return null;  //TODO
    }
}
