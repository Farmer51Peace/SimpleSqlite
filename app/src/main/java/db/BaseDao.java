package db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import db.annotation.DbField;
import db.annotation.DbTable;

public abstract class BaseDao<T> implements IBaseDao<T> {
    private static final String TAG = "BaseDao";
    protected Map<Field, String> cacheMap;
    private boolean isInit = false;
    private SQLiteDatabase database;
    private Class entityClass;
    private String tableName;

    @Override
    public long insert(T entity) {
        if (entity != null) {
            ContentValues values = getContentValues(entity);
            return database.insert(tableName, null, values);
        } else {
            Log.e(TAG, "insert entity == null");
        }
        return 0;
    }

    private ContentValues getContentValues(T entity) {
        ContentValues contentValues = new ContentValues();
        Field fields[] = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String columnName = cacheMap.get(field);
            if (TextUtils.isEmpty(columnName)) {
                continue;
            } else {
                try {
                    contentValues.put(columnName, field.get(entity).toString());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
        return contentValues;
    }

    public synchronized boolean init(Class<T> entityClass, SQLiteDatabase database) {
        if (!isInit) {
            if (!database.isOpen()) {
                Log.e(TAG, "database is not open");
                return false;
            }
            if (!TextUtils.isEmpty(createTable())) {
                database.execSQL(createTable());
            } else {
                Log.e(TAG, "createTable() is empty");
                return false;
            }
            this.entityClass = entityClass;
            this.database = database;
            if (entityClass.getAnnotation(DbTable.class) == null) {
                this.tableName = entityClass.getSimpleName();
            } else {
                this.tableName = entityClass.getAnnotation(DbTable.class).value();
            }
            this.cacheMap = new HashMap<>();
            initCacheMap();
            isInit = true;
        }
        return true;
    }

    private void initCacheMap() {
        String sql = "select * from " + this.tableName + " limit 1,0";
        Cursor cursor = null;
        try {
            cursor = database.rawQuery(sql, null);
            String[] columnNames = cursor.getColumnNames();
            Field[] columnFields = entityClass.getDeclaredFields();
            for (int i = 0; i < columnNames.length; i++) {
                String columnName = columnNames[i];
                for (Field field : columnFields) {
                    String fieldName = null;
                    field.setAccessible(true);
                    if (field.getAnnotation(DbField.class) != null) {
                        fieldName = field.getAnnotation(DbField.class).value();
                    } else {
                        fieldName = field.getName();
                    }
                    if (!TextUtils.isEmpty(fieldName) && columnName.equals(fieldName)) {
                        cacheMap.put(field, columnName);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    protected abstract String createTable();

    @Override
    public long update(T entity, T where) {
        if (entity != null && where != null) {
            Condition condition = new Condition(getContentValues(where));
            return database.update(tableName, getContentValues(entity), condition.getWhereClause(), condition.getWhereArgs());
        } else {
            return 0;
        }
    }

    class Condition {
        private String whereClause;
        private String[] whereArgs;

        public Condition(ContentValues values) {
            StringBuilder stringBuilder = new StringBuilder();
            ArrayList list = new ArrayList();

            stringBuilder.append("1=1");
            Set<String> set = values.keySet();
            Iterator<String> iterator = set.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                stringBuilder.append(" and " + key + "=?");
                list.add(values.get(key));
            }
            this.whereClause = stringBuilder.toString();
            this.whereArgs = (String[]) list.toArray(new String[list.size()]);
        }

        public String getWhereClause() {
            return whereClause;
        }

        public String[] getWhereArgs() {
            return whereArgs;
        }
    }

    @Override
    public int delete(T entity) {
        if (entity != null) {
            Condition condition = new Condition(getContentValues(entity));
            database.delete(tableName, condition.getWhereClause(), condition.getWhereArgs());
        }
        return 0;
    }

    @Override
    public List<T> query(T where) {
        return query(where, null, null, null);
    }

    @Override
    public List<T> query(T where, String orderBy, Integer startIndex, Integer limit) {
        if (where != null) {
            String limitString = null;
            if (startIndex != null && limit != null) {
                limitString = startIndex + " , " + limit;
            }
            Condition condition = new Condition(getContentValues(where));
            Cursor cursor = database.query(tableName, null, condition.getWhereClause(), condition.getWhereArgs(), null, null, orderBy, limitString);
            List<T> result = getResult(cursor, where);
            cursor.close();
            return result;
        }
        return null;
    }

    private List<T> getResult(Cursor cursor, T where) {
        List<T> result = new ArrayList<>();
        if (cursor != null && where != null) {
            while (cursor.moveToNext()) {
                try {
                    T item = (T) where.getClass().newInstance();
                    Set<Field> set = cacheMap.keySet();
                    Iterator<Field> iterator = set.iterator();
                    while (iterator.hasNext()) {

                        Field field = iterator.next();
                        int columnIndex = cursor.getColumnIndex(cacheMap.get(field));
                        Class type = field.getType();
                        if (type == String.class) {
                            field.set(item, cursor.getString(columnIndex));
                        } else if (type == Double.class) {
                            field.set(item, cursor.getDouble(columnIndex));
                        } else if (type == Integer.class) {
                            field.set(item, cursor.getInt(columnIndex));
                        } else if (type == Long.class) {
                            field.set(item, cursor.getLong(columnIndex));
                        } else if (type == byte[].class) {
                            field.set(item, cursor.getBlob(columnIndex));
                        } else {
                            continue;
                        }
                    }
                    result.add(item);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }
        return null;
    }
}
