package com.cr.pn.Sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zy on 2017/2/10.
 */

public class DataBaseManager<T> {

    private static List<SqlDao> sqlDaos;
    private static DataBaseHelper helper;
    private static DataBaseManager instance;

    /**
     * 获取数据库manager.
     * @param context
     * @param databaseName
     * @param databaseVersion
     * @return
     */
    private DataBaseManager(Context context, String databaseName, int databaseVersion){
        helper  = new DataBaseHelper(context, databaseName, databaseVersion);
        sqlDaos = new ArrayList<>();
    }

    public static synchronized DataBaseManager getInstance(Context context, String databaseName, int databaseVersion) {
        if (instance == null) {
            synchronized (DataBaseManager.class) {
                if (instance == null) {
                    instance = new DataBaseManager(context,databaseName,databaseVersion);
                }
            }
        }
        return instance;
    }


    /**
     * 获取dao.
     * @param clazz
     * @return
     * @throws Exception
     */
    public SqlDao getSqlDao(Class<T> clazz) throws Exception {
        SqlDao sqlDao = new SqlDao();
        sqlDaos.add(sqlDao);
        return sqlDao.setUserDao(clazz,helper);
    }

    /**
     * 创建表.
     * @param clazz
     */
    public int createTable(Class<T> clazz){
        if (helper!=null) {
            return helper.createTable(clazz);
        }else {
            throw new NullPointerException("DataBaseManager is Null");
        }
    }

    /**
     * 如果该表不存在则创建该表.
     * @param clazz
     */
    public int createTableIfNotExists(Class<T> clazz){
        if (helper!=null){
            return helper.createTableIfNotExists(clazz);
        }else {
            throw new NullPointerException("DataBaseManager is Null");
        }
    }

    /**
     * 创建表,返回需要运行以创建表的SQL语句的有序集合.
     * @param clazz
     */
    public List<String> getCreateTableStatements(Class<T> clazz){
        if (helper!=null){
            return helper.getCreateTableStatements(clazz);
        }else {
            throw new NullPointerException("DataBaseManager is Null");
        }
    }

    /**
     * 清除表.
     * @param clazz
     */
    public int clearTable(Class<T> clazz){
        if (helper!=null){
            return helper.clearTable(clazz);
        }else {
            throw new NullPointerException("DataBaseManager is Null");
        }
    }

    /**
     * 删除表.
     * @param clazz
     * @return
     */
    public int dropTable(Class<T> clazz){
        if (helper!=null){
            return helper.dropTable(clazz);
        }else {
            throw new NullPointerException("DataBaseManager is Null");
        }
    }

    /**
     * 增加表的字段.
     * @param clazz
     * @return
     */
    public boolean updateADD(Class<T> clazz){
        if (helper!=null){
            return helper.updateADD(clazz);
        }else {
            throw new NullPointerException("DataBaseManager is Null");
        }
    }

    /**
     * 删除表的字段.
     * @param clazz
     * @return
     */
    public boolean updateDELETE(Class<T> clazz){
        if (helper!=null){
            return helper.updateDELETE(clazz);
        }else {
            throw new NullPointerException("DataBaseManager is Null");
        }
    }

    /**
     * 获取SQLiteDatabase对象.
     * 便于直接使用sql语句操作数据库.
     * 但会导致数据库结构和表结构与实体类不同.
     * 谨慎直接使用sql语句操作.
     */
    public SQLiteDatabase getDatabase(){
        if (helper!=null){
            return helper.getWritableDatabase();
        }else {
            throw new NullPointerException("DataBaseManager is Null");
        }
    }

    /**
     * 关闭链接.
     */
    public void close(){
        helper.close();
        for (int i = 0;i<sqlDaos.size();i++){
            SqlDao sqlDao = sqlDaos.get(i);
            sqlDao = null;
        }
        sqlDaos.removeAll(sqlDaos);
        helper = null;
    }

    public void removeDao(SqlDao sqlDao){
        sqlDaos.remove(sqlDao);
        sqlDao = null;
    }
}
