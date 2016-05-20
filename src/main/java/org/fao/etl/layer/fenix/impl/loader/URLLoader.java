package org.fao.etl.layer.fenix.impl.loader;

import org.fao.etl.layer.fenix.Loader;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.io.InputStream;

@ApplicationScoped
public class URLLoader implements Loader<String> {

    @Override
    public InputStream load(String properties) throws IOException {
        return new java.net.URL(properties).openStream();
    }
}
