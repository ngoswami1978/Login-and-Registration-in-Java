package com.neerajweb.myfirstapp.model;

import java.io.Serializable;

/**
 * Created by Admin on 10/08/2015.
 */
public class flat_model implements Serializable {

    public static final String TAG = "Flat";
    private static final long serialVersionUID = -7406082437623008161L;

    private long mId;
    private String mName;
    private String mFlat_Type;

    public flat_model() {}

    public flat_model(String name, String mFlatType) {
        this.mName = name;
        this.mFlat_Type = mFlatType;
    }

    public long getId() {
        return mId;
    }
    public void setId(long mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }
    public void setName(String mFirstName) {
        this.mName = mFirstName;
    }

    public String getFlatType() {
        return mFlat_Type;
    }
    public void setFlatType(String mType) {
        this.mFlat_Type = mType;
    }

}

