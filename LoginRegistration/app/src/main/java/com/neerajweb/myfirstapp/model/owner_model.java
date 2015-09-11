package com.neerajweb.myfirstapp.model;

import java.io.Serializable;

/**
 * Created by Admin on 07/08/2015.
 * Model owner_model
 */
public class owner_model implements Serializable {
    public static final String TAG = "Owner";
    private static final long serialVersionUID = -7406082437623008161L;

    private long mId;
    private String mName;
    private String mFlatno;
    private String mUsername;
    private String mPassword;
    private String mEmail;
    private int mAge;
    private int mStatus;

    public owner_model() {}

    public owner_model(String name,String flatno,String username,String password,String email,int age, int Aprstatus)
    {
        this.mName = name;
        this.mFlatno = flatno;
        this.mUsername = username;
        this.mPassword = password;
        this.mEmail=email;
        this.mAge=age;
        this.mStatus=Aprstatus;
    }

    public long getId() { return mId;  }
    public void setId(long mId) { this.mId = mId; }

    public String getName() {
        return mName;
    }
    public void setName(String mFirstName) {
        this.mName = mFirstName;
    }

    public String getFlatno() {
        return mFlatno;
    }
    public void setFlatno(String mFlatno) {
        this.mFlatno = mFlatno;
    }

    public String getUsername() {
        return mUsername;
    }
    public void setUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getPassword() {
        return mPassword;
    }
    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getEmail() {
        return mPassword;
    }
    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public int getAge() {
        return mAge;
    }
    public void setAge (int mAge) {
        this.mAge = mAge;
    }

    public int getStatus() {
        return mStatus;
    }
    public void setStatus (int mApprovalStatus) {
        this.mStatus= mApprovalStatus;
    }

}
