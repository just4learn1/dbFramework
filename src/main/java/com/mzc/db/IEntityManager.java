package com.mzc.db;

import java.util.List;

public interface IEntityManager<T> {

    /**
     * 获得一个唯一id
     * @return
     */
    long nextId() throws Exception;

    /**
     * 通过主键id获取对象
     * @param id
     * @return
     */
    T getEntity(long id) throws Exception;

    /**
     * 获取对象列表
     * @param ids
     * @return
     */
    List<T> getEntitys(long[] ids) throws Exception;

    /**
     * 通过条件查询数据
     * @param clazz  一个数据表可能会对应一个类及其所有子类，需要使用clazz来确定查询的是具体哪个类的数据
     * @param prepareWhere
     * @param order
     * @param parameter
     * @param start
     * @param end
     * @return
     */
    List<T> getEntitysWithCondition(Class<?> clazz, String prepareWhere, String order, Object[] parameter, long start, long end) throws Exception;

    /**
     * 查询表中部分变量
     * @param clazz 接口，根据接口中定义的get方法得到需要查询的变量名
     * @param ids
     * @param <S>
     * @return
     * @throws Exception
     */
    <S> List<S> queryFields(Class<S> clazz, long[] ids)throws Exception;

    /**
     * 保存一个新的对象
     * @param t
     */
    void insert(T t) throws Exception;

    /**
     *
     * @param t
     * @param field  变量名，用来标记具体更新的是对象的哪个变量
     * @throws Exception
     */
    void update(T t, String field) throws Exception;

    /**
     * count(*)
     * @return
     * @throws Exception
     */
    long count() throws Exception;

    /**
     * 通过条件获取数据库中对应数据条数
     * @param clazz
     * @param prepareWhere
     * @param parameter
     * @return
     * @throws Exception
     */
    long count(Class<?> clazz, String prepareWhere, Object[] parameter) throws Exception;

    void init() throws Exception;
}
