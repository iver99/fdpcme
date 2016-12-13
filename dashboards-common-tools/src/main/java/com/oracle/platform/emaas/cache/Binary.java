package com.oracle.platform.emaas.cache;

/**
 * Created by chehao on 2016/12/11.
 */
public class Binary {
    private byte[] data;

    public Binary(byte[] data)  {
        this.data =data;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }



}