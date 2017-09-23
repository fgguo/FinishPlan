package com.example.finishplan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dell-pc on 2017/3/26.
 */

public class Dailyconclusion extends Activity {
    ListView list;
    //////////////////////////////////////////////////////////////////定义数据存放方式的
    Cursor cursor;                          //定义指向数据的游标
    dataBase data;
    List<Map<String, String>> listitems;              //存放数据库内容的动态数组
    Dailyconclusion.projectAdapter listViewAdapter;                        //自定义容器
    LayoutInflater listContainer;                     //相当于findviewbyid 自定义listview布局
    //////////////////////////////////////////////////////////////////   定义提示框的
    ////////////////////////////////////////////////////////////////////定义optionmenu的选项
    protected final static int MENU_DELETE = Menu.FIRST + 1;
    protected final static int MENU_UPDATE = Menu.FIRST + 2;
    protected final static int MENU_EXIT = Menu.FIRST + 3;
    ////////////////////////////////////////////////////////////////////定义一些中间传递的字符串
    String getListID = "2";
    String ItemID;
    int mposition,getPosition;
    String SWITCH = "cm";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dailyconclusion);
        data = new dataBase(this);
        cursor = data.select(1,1);
        cursor.moveToFirst();
/////////////////////////////////////////////////////////////////////////////////////////	设置listview来装数据
        list = (ListView) findViewById(R.id.projectList);
        listitems = getListItems();
        listViewAdapter = new Dailyconclusion.projectAdapter(listitems, this);
        list.setAdapter(listViewAdapter);
        this.registerForContextMenu(list);
/////////////////////////////////////////////////////////////////////////////////   触发listview点击事件
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                getListID = listitems.get(arg2).get("ID");
                mposition = arg2;
                SWITCH = "om";
            }
        });
    }
    List<Map<String, String>> getListItems() {
        List<Map<String, String>> listItems = new ArrayList<Map<String, String>>();
        listItems.clear();
        for (int i = 0; i < cursor.getCount(); i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("ID", "" + cursor.getString(0));                                      //放ID
            map.put("date", "" + cursor.getString(1));//放日期
            Log.d("日期",""+cursor.getString(1));
            map.put("journal", "" + cursor.getString(2));
            cursor.moveToNext();
            listItems.add(map);
        }
        cursor.moveToFirst();
        return listItems;
    }
    public class projectAdapter extends BaseAdapter {

        private List<Map<String, String>> listItems;                                //接受参数数组
        Context context;

        public final class ListItemView                 //设置listview的内容和格式
        {
            public TextView date;
            public TextView journal;
        }

        public projectAdapter(List<Map<String, String>> listItems, Context context)         //构造函数初始化
        {
            this.context = context;
            listContainer = LayoutInflater.from(context);
            this.listItems = listItems;
        }

        @Override
        public int getCount() {
            return listItems.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        public void getID(int checkedID) {
            String id = listitems.get(checkedID).get("ID").toString();
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {    //最重要的地方  重写getview的方法

            final int selectID = position;
            Dailyconclusion.projectAdapter.ListItemView listItemView = null;
            if (convertView == null) {
                listItemView = new Dailyconclusion.projectAdapter.ListItemView();
                convertView = listContainer.inflate(R.layout.mylistitem, null);                         //设置view布局
                listItemView.journal = (TextView) convertView.findViewById(R.id.Topic); //获得对象  记得加上布局名字后再加findviewbyid
                listItemView.date = (TextView) convertView.findViewById(R.id.ItemDateTime);
                convertView.setTag(listItemView);                                              //设置空间集到convertView
            } else {
                listItemView = (Dailyconclusion.projectAdapter.ListItemView) convertView.getTag();
            }

            listItemView.journal.setText((String) listitems.get(position).get("journal"));      //设置listview的内容
            listItemView.date.setText((String) listitems.get(position).get("date"));
            getID(selectID);
            return convertView;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Dailyconclusion.this, select.class);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(home);
        }
        return true;
    }
}
