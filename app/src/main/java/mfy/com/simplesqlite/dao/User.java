package mfy.com.simplesqlite.dao;

import db.annotation.DbField;
import db.annotation.DbTable;

@DbTable("tb_user")
public class User {
    @DbField("name")
    public String name;
    @DbField("password")
    public String password;

    public User() {
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
