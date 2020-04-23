package com.yinlei.yinleimusic.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 从资源文件中读取数据
 */
public class DataUtils {

    /**
     * 读取资源文件assets中的数据
     * @return
     */
    public static String getJsonFromAssets(Context context, String fileName){
        /**
         * 1. StringBuilder存放读取处的数据
         * 2. AssetManagr资源管理器的open()打开指定的资源文件，返回inputstream
         * 3. InputStreamReader是字节到字符的桥接器。BufferdReader存放读取字符的缓冲去
         * 4. 循环利用BufferdReader的readLine读取每一行的数据。并把读取的数据放入到StringBuilder
         * 5. 返回读取出来的所有数据
         */
        StringBuilder stringBuilder = new StringBuilder();
        AssetManager assetManager = context.getAssets();
        try {
            InputStream inputStream = assetManager.open(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while((line = bufferedReader.readLine())!= null){
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }
}
