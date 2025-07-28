package com.example.ocbccollecting;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ocbccollecting.databinding.ActivityMainBinding;
import com.example.ocbccollecting.sqlite.DBHelper;
import com.example.ocbccollecting.utils.Logs;

/*
首页
 */
public class MainActivity extends AppCompatActivity implements Handler.Callback {
    private ActivityMainBinding binding;
    private final Handler handler = new Handler(Looper.getMainLooper(), this);
    private int MSG = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initData();
        initViewClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MSG++;
        if (MSG > 1) return;
        Logs.d("重启");
        handler.postDelayed(this::launchAPK4, 50_000);
    }

    private void initViewClick() {
        binding.open.setOnClickListener(this::openClick);
        binding.save.setOnClickListener(this::saveClick);
    }

    private void openClick(View view) {
        launchAPK4();
    }

    public void launchAPK4() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ComponentName comp = new ComponentName("com.ocbcnisp.onemobileapp", "com.ocbcnisp.byon.ui.splashscreen.SplashScreenActivity");
        intent.setComponent(comp);
        startActivity(intent);
    }

    private void saveClick(View view) {
        delete("url");
        delete("cardNumber");
        delete("payURL");
        delete("mode");

        String url = binding.url.getText().toString();
        String payURL = binding.payUrl.getText().toString();
        String cardNumber = binding.cardNumber.getText().toString();
        int selectedItemPosition = binding.spinner.getSelectedItemPosition();
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(cardNumber) || TextUtils.isEmpty(payURL)) {
            Toast.makeText(this, "输入不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        save(getContentValues("mode", selectedItemPosition + ""));
        save(getContentValues("url", url));
        save(getContentValues("payURL", payURL));
        save(getContentValues("cardNumber", cardNumber));
        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
    }

    private void save(ContentValues contentValues) {
        try (DBHelper dbHelper = new DBHelper(this)) {
            dbHelper.insert(contentValues);
        }
    }

    private ContentValues getContentValues(String name, String value) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.NAME, name);
        contentValues.put(DBHelper.CONTENT, value);
        return contentValues;
    }

    private void initData() {
        String cardNumber = query("cardNumber");
        String url = query("url");
        String payURL = query("payURL");
        String mode = query("mode");
        if (!TextUtils.isEmpty(url)) binding.url.setText(url);
        if (!TextUtils.isEmpty(cardNumber)) binding.cardNumber.setText(cardNumber);
        if (!TextUtils.isEmpty(payURL)) binding.payUrl.setText(payURL);
        if (!TextUtils.isEmpty(mode)) binding.spinner.setSelection(Integer.parseInt(mode));
    }

    public void delete(String name) {
        try (DBHelper dbHelper = new DBHelper(this)) {
            dbHelper.delete("name=?", new String[]{name});
        }
    }

    public String query(String name) {
        try (DBHelper dbHelper = new DBHelper(this); Cursor cursor = dbHelper.query(
                null,
                "name=?",
                new String[]{name},
                null)) {
            return cursor != null && cursor.moveToFirst() ? cursor.getString(2) : "";
        }
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        return false;
    }
}