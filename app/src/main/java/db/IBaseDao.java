package db;

import java.util.List;

public interface IBaseDao<T> {
    public long insert(T entity);

    public long update(T entity, T where);

    public int delete(T entity);

    public List<T> query(T where);

    public List<T> query(T where, String orderBy, Integer startIndex, Integer limit);

    List<T> query(String sql);
}
