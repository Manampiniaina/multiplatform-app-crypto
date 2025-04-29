package com.crypto.model.reponse;


public class JsonResponse<T> {

    private String status;
    private int code;
    private String error;
    private Object data; // Utilisation générique pour que "data" puisse contenir différents types
    // private T data; // Utilisation générique pour que "data" puisse contenir différents types

    public JsonResponse() {}

    public JsonResponse(String status, int code, String error, T data) {
        this.status = status;
        this.code = code;
        this.error = error;
        this.data = data;
    }

    // Getters et Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        if(error==null || error.isEmpty()) this.error = null;
        else this.error = error;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "JsonResponse{" +
                "status='" + status + '\'' +
                ", code=" + code +
                ", error='" + error + '\'' +
                ", data=" + data +
                '}';
    }
}