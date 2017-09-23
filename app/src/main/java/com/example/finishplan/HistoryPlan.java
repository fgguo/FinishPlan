package com.example.finishplan;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HistoryPlan extends AppCompatActivity {
    ListView list;
    //////////////////////////////////////////////////////////////////定义数据存放方式的
    Cursor cursor;                          //定义指向数据的游标
    dataBase data;
    String finished;
    List<Map<String, String>> listitems;              //存放数据库内容的动态数组
    HistoryPlan.projectAdapter listViewAdapter;                        //自定义容器
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_plan);
        Toast.makeText(this,"这是历史计划！",Toast.LENGTH_SHORT).show();
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
        listViewAdapter = new HistoryPlan.projectAdapter(listitems, this);
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
            map.put("content", "" + cursor.getString(1));         //放优先级和主题
            map.put("date&time", "" + cursor.getString(2) + "," + cursor.getString(3));              //放日期时间
            map.put("finished", cursor.getString(4));                                      //放状态
            cursor.moveToNext();
            listItems.add(map);
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
            HistoryPlan.projectAdapter.ListItemView listItemView = null;
            if (convertView == null) {
                listItemView = new HistoryPlan.projectAdapter.ListItemView();
                convertView = listContainer.inflate(R.layout.my_listitem, null);                         //设置view布局
                listItemView.content = (TextView) convertView.findViewById(R.id.Topic); //获得对象  记得加上布局名字后再加findviewbyid
                listItemView.datetime = (TextView) convertView.findViewById(R.id.ItemDateTime);
                listItemView.check = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(listItemView);                                              //设置空间集到convertView
            } else {
                listItemView = (HistoryPlan.projectAdapter.ListItemView) convertView.getTag();
            }

            listItemView.content.setText((String) listitems.get(position).get("content"));      //设置listview的内容
            listItemView.datetime.setText((String) listitems.get(position).get("date&time"));
            getID(selectID);
            String finished= listitems.get(selectID).get("finished").toString();
            int i=finished.compareTo("已完成!");
            if(i==0)
            {
                listItemView.check.setChecked(true);
            }
            listItemView.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {          //listitem点击事件
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checkedChange(selectID, isChecked);
                }
            });
            return convertView;
        }
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        Log.v("test", "populate context menu");
        menu.setHeaderTitle("选择");                              // 设定 menu 标题
        menu.add(0, 2, Menu.NONE, "删除");
    }
    //////////////////////////////////////////////////////////////////响应上下文菜单项
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // 得到当前被选中的item信息
        super.onContextItemSelected(item);
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();//获得AdapterContextMenuInfo,以此来获得选择的listview项
        ItemID = listitems.get(menuInfo.position).get("ID");
        getPosition = menuInfo.position;
        SWITCH = "cm";
        switch (item.getItemId()) {                    //点击不同项 所触发的不同事件
            case 2:
                delete(ItemID);                             //删除
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }
    ////////////////////////////////////////////////////////////////////       设定optionmenu的方法
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case MENU_DELETE:                          //删除
                delete(getListID);
                break;
        }
        return true;

    }
    public void delete(String getListID)                                //实现删除事件
    {
        final String getlistid = getListID;
        AlertDialog.Builder cancelConfirm = new AlertDialog.Builder(this);
        cancelConfirm.setTitle("确定删除么？");
        cancelConfirm.setPositiveButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "您已取消删除！", Toast.LENGTH_SHORT).show();
            }
        });

        cancelConfirm.setNegativeButton("删除吧", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    int id = cursor.getInt(0);
                    String str = Integer.toString(id);
                    int e = str.compareTo(getlistid);
                    Log.d("whether ID has got", str);
                    Log.d("whether ID has got", getlistid);
                    if (e == 0) {
                        Log.d("判断","成功");
                        String daTe = cursor.getString(2) + cursor.getString(3);
                        Calendar calendar = Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH) + 1;
                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);
                        String Date = new String(" ");
                        if (hour < 10 && minute < 10)//调整时间格式
                        {
                            Date = (year + "年" + month + "月" + day + "日" + "0" + hour + ":" + "0" + minute).toString();
                        }
                        if (hour < 10 && minute >= 10) {
                            Date = (year + "年" + month + "月" + day + "日" + "0" + hour + ":" + minute).toString();
                        }
                        if (hour >= 10 && minute >= 10) {
                            Date = (year + "年" + month + "月" + day + "日" + hour + ":" + minute).toString();
                        }
                        if (hour >= 10 && minute < 10) {
                            Date = (year + "年" + month + "月" + day + "日" + hour + ":" + "0" + minute).toString();
                        }
                        Pattern p = Pattern.compile("\\d{1,}");//连续数字的最少个数
                        String u = daTe;
                        Matcher m = p.matcher(u);
                        List<String> digitList = new ArrayList<String>();
                        while (m.find()) {
                            digitList.add(m.group());
                        }
                        int Year = Integer.valueOf(digitList.get(0));
                        int Month = Integer.valueOf(digitList.get(1));
                        int Day = Integer.valueOf(digitList.get(2));
                        int Hour = Integer.valueOf(digitList.get(3));
                        int Minute = Integer.valueOf(digitList.get(4));
                        if (Hour < 10) {
                            daTe = cursor.getString(2) + "0" + cursor.getString(3);
                        }
                        int result = daTe.compareTo(Date);
                        if (result > 0) {                   //计划时间比当前时间大就设置闹钟
                            Calendar c = Calendar.getInstance();
                            c.set(Calendar.YEAR, Year);
                            c.set(Calendar.MONTH, (Month - 1));//也可以填数字，0-11,一月为0
                            c.set(Calendar.DAY_OF_MONTH, Day);
                            c.set(Calendar.HOUR_OF_DAY, Hour);
                            c.set(Calendar.MINUTE, Minute);
                            c.set(Calendar.SECOND, 0);
//也可以写在一行里
                            Intent inten = new Intent(HistoryPlan.this, Alarmreceiver.class);
                            PendingIntent pii = PendingIntent.getBroadcast(HistoryPlan.this, 0, inten, 0);
////设置一个PendingIntent对象，发送广播
                            AlarmManager amm = (AlarmManager) getSystemService(ALARM_SERVICE);
////获取AlarmManager对象
                            amm.cancel(pii);
//时间到时，执行PendingIntent，只执行一次
//AlarmManager.RTC_WAKEUP休眠时会运行，如果是AlarmManager.RTC,在休眠时不会运行
//如果需要重复执行，使用上面一行的setRepeating方法，倒数第二参数为间隔时间,单位为毫秒
                        }
                    }
                    cursor.moveToNext();
                }
                data.delete(getlistid);
                if (SWITCH.equals("om")) {
                    listitems.remove(mposition);
                }
                if (SWITCH.equals("cm")) {
                    listitems.remove(getPosition);    //对于位置的量要求需精确   不能随便更改类型  否则可能会因为丢失精度而定位错误
                    Log.d("cm", SWITCH);
                }
                listViewAdapter.notifyDataSetChanged();  //重绘当前可见区域
                Toast.makeText(getApplicationContext(), "已经删除了！", Toast.LENGTH_SHORT).show();
            }
        });
        cancelConfirm.show();
        //先移除已经删除的item，便于下面的这个东东检测出view的变化
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
