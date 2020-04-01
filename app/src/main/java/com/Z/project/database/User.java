package com.Z.project.database;

public class User {

    private int _id;
    private String account;
    private String password;

    public int get_id() {
        return _id;
    }
    public String getAccount() {
        return account;
    }
    public String getPassword() {
        return password;
    }
    public void set_id(int _id) {
        this._id = _id;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public User() {
        super();

    }
    //自定义构造函数。
    public User(String account, String password) {
        super();
        this.account = account;
        this.password=password;
    }
}