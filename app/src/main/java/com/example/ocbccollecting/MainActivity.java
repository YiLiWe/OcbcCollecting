package com.example.ocbccollecting;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ocbccollecting.databinding.ActivityMainBinding;
import com.example.ocbccollecting.sqlite.DBHelper;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initData();
        initViewClick();
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

        String url = binding.url.getText().toString();
        String cardNumber = binding.cardNumber.getText().toString();
        int selectedItemPosition = binding.spinner.getSelectedItemPosition();
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(cardNumber)) {
            Toast.makeText(this, "输入不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        save(getContentValues("mode", selectedItemPosition + ""));
        save(getContentValues("url", url));
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
        if (!TextUtils.isEmpty(url)) binding.url.setText(url);
        if (!TextUtils.isEmpty(cardNumber)) binding.cardNumber.setText(cardNumber);
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

}