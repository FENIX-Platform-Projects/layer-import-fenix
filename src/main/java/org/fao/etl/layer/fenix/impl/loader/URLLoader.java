package org.fao.etl.layer.fenix.impl.loader;

import org.fao.etl.layer.fenix.Loader;

import javax.enterprise.context.ApplicationScoped;
import java.io.InputStream;

@ApplicationScoped
public class URLLoader implements Loader<String> {
    @Override
    public InputStream load(String properties) {
        return null;
    }
}
