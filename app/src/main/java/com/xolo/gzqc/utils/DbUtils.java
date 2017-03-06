package com.xolo.gzqc.utils;

import android.content.Context;
import android.database.Cursor;

import org.xutils.DbManager;
import org.xutils.db.sqlite.SqlInfo;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.db.table.DbModel;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.IOException;
import java.util.List;

/**
 * Created by sa on 2016/5/12.
 */
public class DbUtils {
    public final String DB_NAME = "ctd.db";
    private DbManager dbManager;

    public DbUtils(Context ctx){
        DbManager.DaoConfig config = new DbManager.DaoConfig();
        config.setDbName(DB_NAME);
        config.setDbVersion(1);
        dbManager = x.getDb(config);
    }

    public boolean save(Object obj){
        try {
            dbManager.save(obj);
            LogUtil.i("成功");
            return true;
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }
    }

    public<T> List<T> get(Class<T> c){
        try {
            return dbManager.findAll(c);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<DbModel> find(String sql){
        SqlInfo info = new SqlInfo(sql);
        try {
            return dbManager.findDbModelAll(info);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public<T> void delete(Class<T> c, WhereBuilder whereBuilder){
        try {
            int s = dbManager.delete(c, whereBuilder);
            LogUtil.d("TAG", "----------delete:"+s);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    public<T> void delete(Class<T> c){
        try {
             dbManager.delete(c);
            LogUtil.d("TAG", "----------delete:");
        } catch (DbException e) {
            e.printStackTrace();
        }

    }


    public<T> void updata(String sql){
        try {
            int s = dbManager.executeUpdateDelete(sql);
            LogUtil.d("TAG", "----------delete:"+s);
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    public<T> boolean isTable(String tableName){
        try {
            String sql = "select count(*) from sqlite_master where type='table' and name = '"+tableName+"'";
            Cursor cursor = dbManager.execQuery(sql);
            if(cursor.moveToNext()){
                int count = cursor.getInt(0);
                if(count>0){
                    return true;
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void close(){
        try {
            dbManager.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
