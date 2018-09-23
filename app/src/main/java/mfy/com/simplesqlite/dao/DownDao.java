package mfy.com.simplesqlite.dao;

import java.util.List;

import db.BaseDao;

public class DownDao extends BaseDao {
    @Override
    protected String createTable() {
        return "create table if not exists tb_down(tb_time varchar(20),tb_path varchar(10))";
    }

    @Override
    public List query(String sql) {
        return null;
    }
}
