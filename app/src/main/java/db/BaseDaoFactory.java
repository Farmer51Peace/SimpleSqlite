package db;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class BaseDaoFactory {
    private static BaseDaoFactory instance = new BaseDaoFactory();
    private SQLiteDatabase database;
    private String sqliteDatabasePath;

    private BaseDaoFactory() {
        sqliteDatabasePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/teacher.db";
        initDatabase();
    }

    private void initDatabase() {
        this.database = SQLiteDatabase.openOrCreateDatabase(sqliteDatabasePath, null);
    }

    public static BaseDaoFactory getInstance() {
        return instance;
    }

    public <T extends BaseDao<M>, M> T getDbHelper(Class<T> daoClass, Class<M> entityClass) {
        BaseDao baseDao = null;
        try {
            baseDao = daoClass.newInstance();
            baseDao.init(entityClass, database);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (T) baseDao;
    }
}
