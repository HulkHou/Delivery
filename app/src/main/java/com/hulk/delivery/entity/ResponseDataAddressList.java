package com.hulk.delivery.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hulk-out on 2017/11/28.
 */

public class ResponseDataAddressList extends ResponseDataList implements Serializable {

    private List<TAddress> list;

    public List<TAddress> getList() {
        return list;
    }

    public void setList(List<TAddress> list) {
        this.list = list;
    }
}
