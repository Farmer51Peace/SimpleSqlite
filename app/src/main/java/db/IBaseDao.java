package db;

public interface IBaseDao<T> {
    public long insert(T entity);

    public long update(T entity, T where);
}
