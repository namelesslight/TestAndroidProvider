package com.example.testandroidprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

@SuppressLint("Range")
public class MainActivity extends AppCompatActivity {
    Button insertBtn;
    Button updateBtn;
    Button queryBtn;
    Button deleteBtn;
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
            Uri uri=Uri.parse(AUTH);
            Cursor cursor=getContentResolver().query(uri,null,null,null,null);
            if (cursor.moveToFirst()){
                while (cursor.moveToNext()){
                    @SuppressLint("Range") String name =cursor.getString(cursor.getColumnIndex("name"));
                    @SuppressLint("Range") String author =cursor.getString(cursor.getColumnIndex("author"));
                    @SuppressLint("Range") int pages =cursor.getInt(cursor.getColumnIndex("pages"));
                    @SuppressLint("Range") double price =cursor.getDouble(cursor.getColumnIndex("price"));
                    Log.d("MainActivity","book name is"+name);
                    Log.d("MainActivity","book author is"+author);
                    Log.d("MainActivity","book pages is"+pages);
                    Log.d("MainActivity","book price is"+price);
                };
            }
            cursor.close();
        });
    }
}