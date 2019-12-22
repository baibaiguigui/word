
package com.example.c;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class MainActivity extends AppCompatActivity {
    MyDatabaseHelper helper;
    //    private List<String> list;
    ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    ArrayList<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
    ListView lv;
    TextView text2;
    private SimpleAdapter simpleAdapter = null;
    EditText edit1, edit2, edit3;
    SQLiteDatabase db;
    String s1, s2, s3,find;
    String y1,y2,y3;
    String x1,x2,x3;
    String d,f;
    ListView lv1;
    int index = 0;
    ContextMenu contextMenu;

    //菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //查询
            case R.id.c:
                showDialog2();
                break;
            //添加
            case R.id.t:
                showDialog3();
                break;
            //修改
            case R.id.x:
                showDialog1();
                break;
            case R.id.help:
                Toast.makeText(this, "点击是删除", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //修改的提示框
    private void showDialog1() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
        final View view1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.update, null);
        builder1.setTitle("");
        builder1.setView(view1);
        builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText x11=view1.findViewById(R.id.x1);
                EditText x22=view1.findViewById(R.id.x2);
                EditText x33=view1.findViewById(R.id.x3);
                EditText y11=view1.findViewById(R.id.y1);
                EditText y22=view1.findViewById(R.id.y2);
                EditText y33=view1.findViewById(R.id.y3);
                x1=x11.getText().toString();
                y1=y11.getText().toString();
                x2=x22.getText().toString();
                y2=y22.getText().toString();
                x3=x33.getText().toString();
                y3=y33.getText().toString();
                updateData();
                Refresh_Display();
            }
        });
        builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder1.show();
    }

    //查询的提示框
    private void showDialog2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        final View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.find, null);
        builder.setTitle("");
        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText ff= view.findViewById(R.id.find);
                f=ff.getText().toString();
                Cursor cursor = db.query("word",null,null,null,null,null,null);
                if (cursor.moveToFirst()) {
                    do {
                        String english = cursor.getString(cursor.getColumnIndex("english"));
                        String chinese = cursor.getString(cursor.getColumnIndex("chinese"));
                        String con = cursor.getString(cursor.getColumnIndex("con"));
                        String s = "“英文：" + english + " 中文翻译" + chinese + "例句" + con + "”";
                        if(s.contains(f)){
                            d=s;
                            text2.setText(d);
                        }
                    }while(cursor.moveToNext());
                }


            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }
    //添加的提示框
    private void showDialog3() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        final View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.num, null);
        builder.setTitle("");
        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                edit1 = view.findViewById(R.id.english);
                edit2 = view.findViewById(R.id.chinese);
                edit3 = view.findViewById(R.id.con);
                s1 = edit1.getText().toString();
                s2 = edit2.getText().toString();
                s3 = edit3.getText().toString();
                inserData();
                Refresh_Display();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);
        lv = findViewById(R.id.lv);
        text2=findViewById(R.id.text2);
        helper = new MyDatabaseHelper(this, "word.db", null, 2);
        db = helper.getWritableDatabase();
        Refresh_Display();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long arg3) {
                // TODO Auto-generated method stub
                String n=list.get(position).toString();
                int n1=n.indexOf("：");
                int n2=n.indexOf("中");
                String z=n.substring(n1+1,n2);
                int n11=n.indexOf("译");
                int n22=n.indexOf("例");
                String zz=n.substring(n11+1,n22);
                int n111=n.indexOf("句");
                int n222=n.indexOf("”");
                String zzz=n.substring(n111+1,n222);
                db.execSQL("delete from word where english=?", new Object[]{z});
                db.execSQL("delete from word where chinese=?", new Object[]{zz});
                db.execSQL("delete from word where con=?", new Object[]{zzz});
                list.remove(view);
                Toast.makeText(MainActivity.this,z,Toast.LENGTH_SHORT).show();
                Refresh_Display();
            }

        });
    }
    //刷新及显示
    private void Refresh_Display(){
        SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(),
                getData(),
                R.layout.item,
                new String[]{
                        "english"
                },
                new int[]{
                        R.id.textView
                });
        lv.setAdapter(adapter);
    }
    //全部查询
    public List<Map<String, Object>> getData() {
        list.clear();
        String sql = "select * from word";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            String english = cursor.getString(cursor.getColumnIndex("english"));
            String chinese = cursor.getString(cursor.getColumnIndex("chinese"));
            String con = cursor.getString(cursor.getColumnIndex("con"));
            String s = "“英文："+english +" 中文翻译" + chinese + "例句" + con+"”";
            map.put("english", s);
            list.add(map);
        }
        return list;
    }
    //添加
    private void inserData() {
        ContentValues values;
        values = new ContentValues();
        if(s1!=null&&s2!=null&&s3!=null) {
            values.put("english", s1);
            values.put("chinese", s2);
            values.put("con", s3);
            db.insert("word", null, values);
            values.clear();
        }
        else{

        }
    }

    private void updateData() {
        ContentValues values = new ContentValues();//是用map封装的对象，用来存放值
        values.put("english",y1);
        db.update("word",values, "english = ?", new String[]{x1});
        values.clear();
        values.put("chinese",y2);
        db.update("word",values, "chinese= ?", new String[]{x2});
        values.clear();
        values.put("con",y3);
        db.update("word",values, "con= ?", new String[]{x3});
        values.clear();
    }
}

