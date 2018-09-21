package db;

import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseDao<T> implements IBaseDao<T> {
    protected Map<String, Field> cacheMap = new HashMap<>();

    @Override
    public long insert(T entity) {

        return 0;
    }

    public void init(Class entityClass, SQLiteDatabase sqLiteDatabase) {
        
    }
}
