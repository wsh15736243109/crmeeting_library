package com.cr.pn.Sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;


/**
 * Created by zy on 2017/2/9.
 * 数据库操作辅助类.
 */
public class DataBaseHelper<T> extends OrmLiteSqliteOpenHelper{

    private Dao<T, Integer> userDao;

    protected DataBaseHelper(Context context, String databaseName, int databaseVersion) {
        super(context, databaseName, null, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    protected int createTable(Class<T> clazz){
        try{
            return TableUtils.createTable(connectionSource, clazz);
        } catch (SQLException e){
            e.printStackTrace();
            return -1;
        }
    }

    protected int createTableIfNotExists(Class<T> clazz){
        try{
            return TableUtils.createTableIfNotExists(connectionSource, clazz);
        } catch (SQLException e){
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 返回需要运行以创建表的SQL语句的有序集合.
     * @param clazz
     */
    protected List<String> getCreateTableStatements(Class<T> clazz){
        try{
            return TableUtils.getCreateTableStatements(connectionSource, clazz);
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 清除表.
     * @param clazz
     * @return
     */
    protected int clearTable(Class<T> clazz){
        try{
            return TableUtils.clearTable(connectionSource, clazz);
        } catch (SQLException e){
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 删除表.
     * @param clazz
     * @return
     */
    protected int dropTable(Class<T> clazz){
        try{
            return TableUtils.dropTable(connectionSource, clazz,true);
        } catch (SQLException e){
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 增加表的字段.
     * @param clazz
     */
    protected boolean updateADD(Class<T> clazz){
       return DataTableUtil.upgradeTable(this.getWritableDatabase(),connectionSource,clazz,DataTableUtil.OPERATION_TYPE.ADD);
    }

    /**
     * 删除表的字段.
     * @param clazz
     */
    protected boolean updateDELETE(Class<T> clazz){
        return DataTableUtil.upgradeTable(this.getWritableDatabase(),connectionSource,clazz,DataTableUtil.OPERATION_TYPE.DELETE);
    }

    /**
     * 释放资源
     */
    @Override
    public void close()
    {
        super.close();
        userDao = null;
    }
}
