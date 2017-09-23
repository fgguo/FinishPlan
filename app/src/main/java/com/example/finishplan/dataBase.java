package com.example.finishplan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class dataBase extends SQLiteOpenHelper {
    /////////////////////////////////////////////////////////////////////////定义数据库字段名
    private static String DATABASE_NAME="plan.db";
    private static String TABLE_NAME="plans";
    private static String ID="project_id";
    private static String DATE="date";
    private static String TIME="time";
    private static String CONTENT="content";
    private static String FINISHED="finished";
    private static String FPSWDPRO="fpswdpro";
    private static String SPSWDPRO="spswdpro";
    private static String TPSWDPRO="tpswdpro";
    private static String PASSWORD="password";
    private static String JOURNAL="journal";
    private static String MEMORY="memory";
    private Context mContext;
    ///////////////////////////////////////////////////////////////////////构造方法
    String sql = "CREATE TABLE plans("
            + "ID integer primary key autoincrement, "
            + "CONTENT  text,"
            + "DATE  text,"
            + "TIME  text,"
            + "FINISHED text)";
    String SQL="CREATE TABLE journals("
            + "ID integer primary key autoincrement,"
            + "DATE text,"
            + "JOURNAL text)";
    String sqL="CREATE TABLE passwordpro("
            + "ID integer primary key autoincrement,"
            +" FPSWDPRO text,"
            +" SPSWDPRO text,"
            +" TPSWDPRO text)";
    String SQl="CREATE TABLE password("
            + "ID integer primary key autoincrement,"
            +" PASSWORD text)";
    String Sql="CREATE TABLE memory("
            + "ID integer primary key autoincrement,"
            + "DATE  text,"
            +" MEMORY text)";
    public dataBase(Context context) {
        super(context, DATABASE_NAME, null,8);
        mContext=context;
    }
    /////////////////////////////////////////////////////////////////////////创建数据表
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(sql);
        db.execSQL(SQL);
        db.execSQL(sqL);
        db.execSQL(SQl);
        db.execSQL(Sql);
    }
    /////////////////////////////////////////////////////////////////////////插入数据的方法
    public long insert(String content,String date,String time,String finished)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(CONTENT,content);
        cv.put(FINISHED,finished);
        cv.put(DATE, date);
        cv.put(TIME, time);
        long row= db.insert(TABLE_NAME, null, cv);
        return row;
    }
    public long insert(String date,String journal)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(JOURNAL,journal);
        cv.put(DATE, date);
        long row= db.insert("journals", null, cv);
        return row;
    }
    public long insert(String q1,String q2,String q3)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(FPSWDPRO,q1);
        cv.put(SPSWDPRO,q2);
        cv.put(TPSWDPRO,q3);
        long row= db.insert("passwordpro", null, cv);
        return row;
    }
    public long insert(String password)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(PASSWORD,password);
        long row= db.insert("password", null, cv);
        return row;
    }
    public long insert(String memory,String date ,int memo)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(DATE, date);
        cv.put(MEMORY,memory);
        long row= db.insert("memory", null, cv);
        return row;
    }
    /////////////////////////////////////////////////////////////////////////删除

    public void delete(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] whereValue={id};
        db.delete(TABLE_NAME, " ID=?", whereValue);
    }
    ////////////////////////////////////////////////////////////////////////修改数据方法
    public void update(String id,String content,String date,String time,String finished)

    {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] whereValue = {id};
        ContentValues cv = new ContentValues();
        cv.put(CONTENT,content);
        cv.put(FINISHED,finished);
        cv.put(DATE, date);
        cv.put(TIME, time);
        db.update(TABLE_NAME, cv, "ID=?", whereValue);
    }
    public void update(String id,String content,String date,String time)

    {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] whereValue = {id};
        ContentValues cv = new ContentValues();
        cv.put(CONTENT,content);
        cv.put(DATE, date);
        cv.put(TIME, time);
        db.update(TABLE_NAME, cv, "ID=?", whereValue);
    }
    public void update(String id,String password)

    {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] whereValue = {id};
        ContentValues cv = new ContentValues();
        cv.put(PASSWORD,password);
        db.update("password", cv, "ID=?", whereValue);
    }
    /////////////////////////////////////////////////////////////////////////插入数据中的状态的方法
    public void modifyState(String id,String finished)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] whereValue = {id};
        ContentValues cv = new ContentValues();
        cv.put(FINISHED, finished);
        db.update(TABLE_NAME, cv, "ID=?", whereValue);
    }
    public void modifyState(String id,String password,int i)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] whereValue = {id};
        ContentValues cv = new ContentValues();
        cv.put(PASSWORD, password);
        db.update("password", cv, "ID=?", whereValue);
    }
    /////////////////////////////////////////////////////////////////////////当表存在时则删除再创建
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("executing","insert initsql");
        String sql="DROP TABLE IF EXISTS " + TABLE_NAME;
        String SQL="DROP TABLE IF EXISTS " + "journals";
        String SQl="DROP TABLE IF EXISTS " + "passwordpro";
        String sqL="DROP TABLE IF EXISTS " + "password";
        String Sql="DROP TABLE IF EXISTS " + "memory";
        db.execSQL(sql);
        db.execSQL(SQL);
        db.execSQL(SQl);
        db.execSQL(sqL);
        db.execSQL(Sql);
        onCreate(db);
    }
    /////////////////////////////////////////////////////////////////////////选择所有数据
    public Cursor select(int i)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.query(TABLE_NAME, null, null, null, null, null, null);
        return cursor;
    }
    public Cursor select(int i,int j)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.query("journals", null, null, null, null, null, null);
        return cursor;
    }
    public Cursor select(int i,int j,int k)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.query("passwordpro", null, null, null, null, null, null);
        return cursor;
    }
    public Cursor select(int i,int j,int k,int l)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.query("password", null, null, null, null, null, null);
        return cursor;
    }
    public Cursor select(int i,int j,int k,int l,int m)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.query("memory", null, null, null, null, null, null);
        return cursor;
    }
    /////////////////////////////////////////////////////////////////////////插入特定id对应的数据
    public Cursor select(String id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String[] selectionArgs={id};
        Cursor cursor=db.query(TABLE_NAME, null, "ID=?", selectionArgs, null, null, null);
        return cursor;
    }
}
