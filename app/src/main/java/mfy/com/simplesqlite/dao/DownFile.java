package mfy.com.simplesqlite.dao;

import db.annotation.DbField;
import db.annotation.DbTable;

@DbTable("tb_down")
public class DownFile {
    public DownFile(String time, String path) {
        this.time = time;
        this.path = path;
    }

    @DbField("tb_time")
    public String time;
    @DbField("tb_path")
    public String path;
}
