package com.example.ocbccollecting.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileUtils {

    /**
     * 追加内容到缓存文件
     * @param context Context
     * @param fileName 文件名（如 "log.txt"）
     * @param content 要追加的内容
     */
    public static void appendToCacheFile(Context context, String fileName, String content) {
        File cacheDir = context.getCacheDir(); // 内部缓存
        File file = new File(cacheDir, fileName);

        try (FileOutputStream fos = new FileOutputStream(file, true)) {
            fos.write(content.getBytes(StandardCharsets.UTF_8));
            Log.d("FileUtils", "追加成功: " + file.getAbsolutePath());
        } catch (IOException e) {
            Log.e("FileUtils", "追加失败", e);
        }
    }

    /**
     * 读取缓存文件内容（可选）
     */
    public static String readCacheFile(Context context, String fileName) {
        File cacheDir = context.getCacheDir();
        File file = new File(cacheDir, fileName);

        try {
            return new String(java.nio.file.Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            Log.e("FileUtils", "读取失败", e);
            return null;
        }
    }
}
