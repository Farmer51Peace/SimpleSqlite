package mfy.com.simplesqlite;

import db.annotation.DbField;
import db.annotation.DbTable;

@DbTable("tb_user")
public class User {
    @DbField("tb_name")
    public String name;
    @DbField("tb_password")
    public String password;
}
