package org.fao.etl.layer.fenix.dto;

public class WMSImportParameters {

    private String wmsUrl;
    private String d3sUrl;

    public String getWmsUrl() {
        return wmsUrl;
    }

    public void setWmsUrl(String wmsUrl) {
        this.wmsUrl = wmsUrl;
    }

    public String getD3sUrl() {
        return d3sUrl;
    }

    public void setD3sUrl(String d3sUrl) {
        this.d3sUrl = d3sUrl;
    }
}
