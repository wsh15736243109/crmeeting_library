package com.cr.pn.Sql;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedUpdate;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

/**
 * Created by zy on 2017/2/9.
 * 数据库表操作.
 */
public class SqlDao<T> {

    private Dao<T, Integer> dao;

    protected SqlDao(){}


    /**
     * 获取dao.
     * @param clazz
     */
    protected SqlDao setUserDao(Class<T> clazz, DataBaseHelper helper) throws SQLException {
        dao = helper.getDao(clazz);
        return this;

    }

    /**
     * 创建一条数据.
     * @param t
     * @return
     */
    public int create(T t){
        try {
            return dao.create(t);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 如果该数据不存在，则创建.
     * @param t
     * @return
     */
    public T createIfNotExists(T t){
        try {
            return dao.createIfNotExists(t);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 创建集合数据.
     * @param t
     * @return
     */
    public void createAll(List<T> t){
        try {
            for (int i = 0;i<t.size();i++){
                dao.createIfNotExists(t.get(i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除一条数据.
     * @param t
     * @return
     */
    public int delete(T t){
        try {
            return dao.delete(t);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 删除多条数据.
     * @param t
     * @return
     */
    public int delete(Collection<T> t){
        try {
            return dao.delete(t);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 根据id删除.
     * @param id
     * @return
     */
    public int deleteId(int id){
        try {
            return dao.deleteById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 根据多条id删除.
     * @param ids
     * @return
     */
    public int deleteIds(Collection<Integer> ids){
        try {
            return dao.deleteIds(ids);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 创建一个删除生成器,进程复杂条件删除.
     * @return
     */
    public DeleteBuilder<T, Integer> deleteBuilder(){
            return dao.deleteBuilder();
    }

    /**
     * 返回表中所有数据.
     * @return
     */
    public List<T> queryForAll(){
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据id返回表中数据.
     * @return
     */
    public T queryForId(int id){
        try {
            return dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 创建一个查询生成器:进行复杂查询.
     * @return
     */
    public QueryBuilder<T, Integer> queryBuilder(){
        return dao.queryBuilder();
    }

    /***
     * 创建或者更新数据.
     * @param t
     * @return
     */
    public Dao.CreateOrUpdateStatus createOrUpdate(T t){
        try {
            return dao.createOrUpdate(t);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 更新数据.
     * @param t
     * @return
     */
    public int update(T t){
        try {
            return dao.update(t);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 更新多条数据.
     * @param ts
     * @return
     */
    public int update(PreparedUpdate<T> ts){
        try {
            return dao.update(ts);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 创建修条件生成器,进行复杂条件修改.
     * @return
     */
    public UpdateBuilder<T, Integer> update(){
        return dao.updateBuilder();
    }


}
