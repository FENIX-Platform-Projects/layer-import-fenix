package org.fao.etl.layer.fenix;

import org.fao.fenix.commons.utils.Properties;

import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;

@Singleton
public class InitProperties extends Properties {
    private static final String configFilePath = "/etl/layer/fenix/importConfig.properties";

    public InitProperties() throws IOException {
        super();
        InputStream configInput = getClass().getResourceAsStream(configFilePath);
        if (configInput!=null)
            load(configInput);
    }
}
