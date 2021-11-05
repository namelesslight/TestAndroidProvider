package com.example.testandroidprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

/**
 * @author ZCL
 * 实现类
 */
public class MainActivity extends AppCompatActivity {
    Button insertBtn;
    Button updateBtn;
    Button queryBtn;
    Button deleteBtn;
    //此处为访问AndroidSQLite项目的url
    private final String AUTH="content://com.example.androidsqlite.provider/book";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        insertBtn=findViewById(R.id.insertBtn);
        updateBtn=findViewById(R.id.updateBtn);
        queryBtn=findViewById(R.id.queryBtn);
        deleteBtn=findViewById(R.id.deleteBtn);

        queryBtn.setOnClickListener(view -> {
            //此处后缀的id可修改，若删除后面字符串只保留AUTH变量会查询全部记录
            Uri uri=Uri.parse(AUTH+"/1");
            Cursor cursor=getContentResolver().query(uri,null,null,null,null);
            if(cursor!=null){
                if (cursor.moveToFirst()){
                    while (cursor.moveToNext()){
                        @SuppressLint("Range") int id =cursor.getInt(cursor.getColumnIndex("id"));
                        @SuppressLint("Range") String name =cursor.getString(cursor.getColumnIndex("name"));
                        @SuppressLint("Range") String author =cursor.getString(cursor.getColumnIndex("author"));
                        @SuppressLint("Range") int pages =cursor.getInt(cursor.getColumnIndex("pages"));
                        @SuppressLint("Range") double price =cursor.getDouble(cursor.getColumnIndex("price"));
                        Log.d("MainActivity","book id is:"+id);
                        Log.d("MainActivity","book name is:"+name);
                        Log.d("MainActivity","book author is:"+author);
                        Log.d("MainActivity","book pages is:"+pages);
                        Log.d("MainActivity","book price is:"+price);
                    }
                }
            }
            if (cursor!=null){
                try {
                    cursor.close();

                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        });
        //插入新的记录
        insertBtn.setOnClickListener(view -> {
            Uri uri=Uri.parse(AUTH);
            ContentValues values=new ContentValues();
            values.put("name","The Old Man and the Sea");
            values.put("author","Ernest Miller Hemingway");
            values.put("pages",200);
            values.put("price",49.9);
            getContentResolver().insert(uri,values);
        });

        //此处用于测试修改单个记录
        updateBtn.setOnClickListener(view -> {
            //此处后缀的id可修改，若删除后面字符串只保留AUTH变量会修改全部记录
            Uri uri=Uri.parse(AUTH+"/1");
            ContentValues values=new ContentValues();
            values.put("price",39.9);
            getContentResolver().update(uri,values,null,null);
        });

        deleteBtn.setOnClickListener(view -> {
            //此处后缀的id可修改，若删除后面字符串只保留AUTH变量会删除全部记录
            Uri uri=Uri.parse(AUTH+"/1");
            getContentResolver().delete(uri,null,null);
        });
    }
}