package com.hulk.delivery.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hulk-out on 2017/11/28.
 */

public class ResponseDataObjectList<T> extends ResponseDataList implements Serializable {

    private List<T> list;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
