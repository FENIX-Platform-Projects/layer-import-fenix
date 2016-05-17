package org.fao.etl.layer.fenix;

import java.io.InputStream;

public interface Loader<T> {

    public InputStream load(T properties);
}
