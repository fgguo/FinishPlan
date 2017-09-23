package com.example.finishplan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class CheckPlan extends AppCompatActivity {
    ListView list;
    //////////////////////////////////////////////////////////////////定义数据存放方式的
    Cursor cursor;                          //定义指向数据的游标
    dataBase data;
    List<Map<String, String>> listitems;              //存放数据库内容的动态数组
    CheckPlan.projectAdapter listViewAdapter;                        //自定义容器
    LayoutInflater listContainer;                     //相当于findviewbyid 自定义listview布局
    //////////////////////////////////////////////////////////////////   定义提示框的
    String finished;
    String getListID = "2";
    int  mposition;
    String SWITCH = "cm";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(this,"请勾选已完成的计划",Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_plan);
        data = new dataBase(this);
        cursor = data.select(1);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            cursor.moveToNext();
        }
        cursor.moveToFirst();
/////////////////////////////////////////////////////////////////////////////////////////	设置listview来装数据
        list = (ListView) findViewById(R.id.projectList);
        listitems = getListItems();
        listViewAdapter = new CheckPlan.projectAdapter(listitems, this);
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
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            String Date = year + "年" + month + "月" + day + "日";
            String date = cursor.getString(2);
            int c = date.compareTo(Date);
            if (c == 0) {
                String finish=cursor.getString(4);
                int d=finish.compareTo("未完成!");
                if(d==0) {
                    map.put("ID", "" + cursor.getString(0));                                      //放ID
                    map.put("content", "" + cursor.getString(1));         //放优先级和主题
                    map.put("date&time", "" + cursor.getString(2) + "," + cursor.getString(3));              //放日期时间
                    map.put("finished", cursor.getString(4));//放状态
                    map.put("Date", cursor.getString(2));
                    listItems.add(map);
                }
            }
            cursor.moveToNext();
        }
        cursor.moveToFirst();
        return listItems;
    }
    public class projectAdapter extends BaseAdapter {
        private List<Map<String, String>> listItems;                                //接受参数数组
        Context context;
        private boolean[] hasChecked;                                       //记录复选框状态

        public final class ListItemView                 //设置listview的内容和格式
        {
            public TextView content;
            public TextView datetime;
            public CheckBox check;
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

        public boolean hasChecked(int checkedID) {
            return hasChecked[checkedID];
        }

        public void getID(int checkedID) {
            String id = listitems.get(checkedID).get("ID").toString();
        }

        private void checkedChange(int checkedID, boolean isChecked)              //checkbox的状态处理 方法 标记事件是否完成
        {
            if (isChecked == true) {
                finished = "已完成!";
                data.modifyState(listitems.get(checkedID).get("ID").toString(), finished);
                Toast.makeText(getApplicationContext(), "计划已完成!", Toast.LENGTH_SHORT).show();
            }
            else {
                finished = "未完成!";
                data.modifyState(listitems.get(checkedID).get("ID").toString(), finished);
                Toast.makeText(getApplicationContext(), "革命尚未成功!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {    //最重要的地方  重写getview的方法
            final int selectID = position;
            CheckPlan.projectAdapter.ListItemView listItemView = null;
            if (convertView == null) {
                listItemView = new CheckPlan.projectAdapter.ListItemView();
                convertView = listContainer.inflate(R.layout.my_listitem, null);                         //设置view布局
                listItemView.content = (TextView) convertView.findViewById(R.id.Topic); //获得对象  记得加上布局名字后再加findviewbyid
                listItemView.datetime = (TextView) convertView.findViewById(R.id.ItemDateTime);
                listItemView.check = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(listItemView);                                              //设置空间集到convertView
            } else {
                listItemView = (CheckPlan.projectAdapter.ListItemView) convertView.getTag();
            }
            listItemView.content.setText((String) listitems.get(position).get("content"));      //设置listview的内容
            listItemView.datetime.setText((String) listitems.get(position).get("date&time"));
            getID(selectID);
            String isfinished= listitems.get(selectID).get("finished").toString();
            int i=isfinished.compareTo("已完成!");
            listItemView.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {          //listitem点击事件
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checkedChange(selectID, isChecked);
                }
            });
            if(i==0)
            {
                listItemView.check.setChecked(true);
            }
            return convertView;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        }
}
