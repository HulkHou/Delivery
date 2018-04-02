package com.hulk.delivery.entity;

import java.io.Serializable;
import java.util.List;

public class GoogleAddressResponseResult implements Serializable {

    private List<Results> results;
    private String status;

    public void setResults(List<Results> results) {
        this.results = results;
    }

    public List<Results> getResults() {
        return results;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}