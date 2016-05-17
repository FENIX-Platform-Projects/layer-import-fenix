package org.fao.etl.layer.fenix.dto;

public class ImportStatus<T> {

    private ErrorType error;
    private String errorMessage;
    private Status status;
    private T properties;



    public ImportStatus(ErrorType error, String errorMessage) {
        status = Status.error;
        this.error = error;
        this.errorMessage = errorMessage;
    }

    public ImportStatus(Status status) {
        this.status = status;
    }
    public ImportStatus() { }



    public ErrorType getError() {
        return error;
    }

    public void setError(ErrorType error) {
        this.error = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public T getProperties() {
        return properties;
    }

    public void setProperties(T properties) {
        this.properties = properties;
    }
}
