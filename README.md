# TestAndroidProvider

> 需要android11(API等级30)
>
> 官网下载android studio
>
> [android studio下载地址]([Download Android Studio and SDK tools  | Android Developers (google.cn)](https://developer.android.google.cn/studio))
>
> 要在本地Android虚拟机上安装https://github.com/namelesslight/AndroidSQLite 项目

## 项目的创建

> 用于访问访问 https://github.com/namelesslight/AndroidSQLite 项目中的数据，以此来验证https://github.com/namelesslight/AndroidSQLite 中的内容提供器可用

### 一、创建项目

在Android studio中新建项目，项目类型为Empty Activity，项目名为TestAndroidProvider

### 二、新建布局

在gradle构建完成之后在app/src/main/res/layout目录下修改activity_main.xml,修改代码如下

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:orientation="vertical"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <Button
        android:id="@+id/insertBtn"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="insertBtn"/>
    <Button
        android:id="@+id/updateBtn"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="updateBtn"/>
    <Button
        android:id="@+id/deleteBtn"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="deleteBtn"/>
    <Button
        android:id="@+id/queryBtn"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="queryBtn"/>
</LinearLayout>
```

### 三、实现代码

在app/src/main/java/com.example.testandroidprovider目录下修改代码，代码如下

```java
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
                        @SuppressLint("Range") int id =cursor
                            .getInt(cursor.getColumnIndex("id"));
                        @SuppressLint("Range") String name =cursor
                            .getString(cursor.getColumnIndex("name"));
                        @SuppressLint("Range") String author =cursor
                            .getString(cursor.getColumnIndex("author"));
                        @SuppressLint("Range") int pages =cursor
                            .getInt(cursor.getColumnIndex("pages"));
                        @SuppressLint("Range") double price =cursor
                            .getDouble(cursor.getColumnIndex("price"));
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
```

### 四、在AndroidManifest.xml中注册

在Android11(api等级30)中，需要在AndroidManifest.xml中注册查询，否则无法获取其他应用的数据

需要在`<manifest/>`标签中添加如下代码

```xml
<queries>
        <package android:name="com.example.androidsqlite"/>
    </queries>
```

修改后AndroidManifest.xml代码如下:

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example.testandroidprovider">
    <queries>
        <package android:name="com.example.androidsqlite"/>
    </queries>
    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.TestAndroidProvider">
        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```
之后就可以测试数据的共享了
