package mfy.com.simplesqlite.dao;

import java.util.List;

import db.BaseDao;

public class UserDao extends BaseDao {

    @Override
    protected String createTable() {
        return "create table if not exists tb_user(name varchar(20),password varchar(10))";
    }

    @Override
    public List query(String sql) {
        return null;
    }
}
