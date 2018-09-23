package mfy.com.simplesqlite;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

import db.BaseDao;
import db.DaoFactory;
import mfy.com.simplesqlite.dao.DownDao;
import mfy.com.simplesqlite.dao.DownFile;
import mfy.com.simplesqlite.dao.User;
import mfy.com.simplesqlite.dao.UserDao;

public class MainActivity extends AppCompatActivity {
    private BaseDao<User> userDao;
    private BaseDao<DownFile> downDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userDao = DaoFactory.getInstance().getDbHelper(UserDao.class, User.class);
        downDao = DaoFactory.getInstance().getDbHelper(DownDao.class, DownFile.class);
    }

    public void insert(View view) {
//        downDao.insert(new DownFile("2018-9-23", "/data/data/1.html"));
        User user = new User("张三", "12345");

        userDao.insert(user);

    }

    public void update(View view) {
        userDao.update(new User("李四", "54321"), new User("张三", "12345"));
    }

    public void delete(View view) {
        userDao.delete(new User("李四", "54321"));
    }

    public void query(View view) {
        List<User> result = userDao.query(new User("张三", "12345"));
        for (User user : result) {
            System.out.println("username = " + user.name + ",password = " + user.password);
        }
    }
}
