package com.example.ocbccollecting.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ocbccollecting.utils.Logs;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE = "test.db";
    private static final String TABLE = "user";
    public static final String NAME = "name";
    public static final String CONTENT = "content";

    public DBHelper(Context context) {
        super(context, DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table = "create table " + TABLE + "(id int primary key, " + NAME + " text not null," + CONTENT + " text not null)";
        db.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    /**
     * 查询数据
     */
    public Cursor query(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(TABLE, projection, selection, selectionArgs, null, null, sortOrder);
    }

    /**
     *删除数据
     */
    public int delete(String selection, String[] selectionArgs) {
        SQLiteDatabase db = getWritableDatabase();
        int result = 0; // 默认返回 0 表示没有删除任何行
        try {
            db.beginTransaction(); // 开始事务
            result = db.delete(TABLE, selection, selectionArgs);
            db.setTransactionSuccessful(); // 标记事务成功
        } catch (Exception e) {
            Logs.log("数据库删除错误：" + e.getMessage());
        } finally {
            db.endTransaction(); // 结束事务
            db.close(); // 关闭数据库连接
        }
        return result;
    }


    /**
     * 插入数据
     */
    public long insert(ContentValues cv) {
        SQLiteDatabase db = getWritableDatabase();
        long result = -1; // 默认返回 -1 表示插入失败
        try {
            db.beginTransaction(); // 开始事务
            result = db.insert(TABLE, null, cv);
            db.setTransactionSuccessful(); // 标记事务成功
        } catch (Exception e) {
            Logs.d("数据库写入错误：" + e.getMessage());
        } finally {
            db.endTransaction(); // 结束事务
            db.close(); // 关闭数据库连接
        }
        return result;
    }
}
