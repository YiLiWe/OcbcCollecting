package com.example.ocbccollecting.sqlite;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;

public class MyProvider extends ContentProvider {
    private DBHelper mUserDBHelper;
    private final static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH); //用于匹配URI，并返回对应的操作编码
    public static final String AUTHORITES = "com.example.ocbccollecting.MyProvider";
    private static final int QUERY = 1; //查询操作编码
    private static final int INSERT = 2; //插入操作编码
    private static final int DELETE = 3; //删除操作编码
    private static final int UPDATE = 4; //更新操作编码

    static { //添加有效的 URI 及其编码
        sUriMatcher.addURI(AUTHORITES, "/query", QUERY);
        sUriMatcher.addURI(AUTHORITES, "/insert", INSERT);
        sUriMatcher.addURI(AUTHORITES, "/delete", DELETE);
        sUriMatcher.addURI(AUTHORITES, "/update", UPDATE);
    }

    @Override
    public boolean onCreate() {
        mUserDBHelper = new DBHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int code = sUriMatcher.match(uri);
        if (code == QUERY) {
            return mUserDBHelper.query(projection, selection, selectionArgs, sortOrder);
        }
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        int code = sUriMatcher.match(uri);
        if (code == INSERT) {
            mUserDBHelper.insert(values);
        }
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null); //通知外界，数据发生变化
        }
        return null;
    }

    @Override
    public String getType(@NonNull Uri uri) { //获取资源 MIME
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int code = sUriMatcher.match(uri);
        if (code == DELETE) {
            return mUserDBHelper.delete(selection, selectionArgs);
        }
        return -1;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return -1;
    }
}