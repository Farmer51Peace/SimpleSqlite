package db;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

public class DaoFactory {
    private static final String TAG = "DaoFactory";
    private static DaoFactory instance = new DaoFactory();
    private SQLiteDatabase database;
    private String sqliteDatabasePath;

    private DaoFactory() {
        sqliteDatabasePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/teacher.db";
        initDatabase();
    }

    private void initDatabase() {
        this.database = SQLiteDatabase.openOrCreateDatabase(sqliteDatabasePath, null);
    }

    public static DaoFactory getInstance() {
        return instance;
    }

    public synchronized <T extends BaseDao<M>, M> T getDbHelper(Class<T> daoClass, Class<M> entityClass) {
        BaseDao baseDao = null;
        boolean initSuccess = false;
        try {
            baseDao = daoClass.newInstance();
            initSuccess = baseDao.init(entityClass, database);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (initSuccess) {
            return (T) baseDao;
        } else {
            Log.e(TAG, "BaseDao init failed");
            return null;
        }
    }
}
