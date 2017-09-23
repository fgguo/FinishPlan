package com.example.finishplan;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.os.Bundle;
import android.app.AlertDialog;;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ContextMenu;
import android.support.v7.app.AppCompatActivity;
import com.getbase.floatingactionbutton.FloatingActionButton;



public class MainActivity extends AppCompatActivity {
    //////////////////////////////////////////////////////////////////定义基本控件
    private DrawerLayout mDrawerLayout;
    ListView list;
    //////////////////////////////////////////////////////////////////定义数据存放方式的
    Cursor  cursor;                          //定义指向数据的游标
    dataBase data;// 数据库
    List<Map<String, String>> listitems;              //存放数据库内容的动态数组
    projectAdapter listViewAdapter;                        //自定义容器
    LayoutInflater listContainer;                     //相当于findviewbyid 自定义listview布局
    ////////////////////////////////////////////////////////////////////定义一些中间传递的字符串
    String ItemID;
    String getListID = "2";
    int  mposition, getPosition;
    String SWITCH = "cm";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.detailed_action_share);
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                        Intent turn = new Intent(MainActivity.this, next.class);
                        startActivityForResult(turn, 1);
                        //跳转到下一页
                    }
                });
        FloatingActionButton fa=(FloatingActionButton)findViewById(R.id.detailed_action_like);
        fa.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
              Toast.makeText(MainActivity.this,"写下今日总结吧！",Toast.LENGTH_SHORT).show();
                Intent tur = new Intent(MainActivity.this, conclusion.class);
                startActivity(tur);
                //跳转到下一页
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setItemIconTintList(null);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.nav_history:
                        Intent intent=new Intent(MainActivity.this,HistoryPlan.class);
                        startActivity(intent);
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_personalSpace:
                        Intent inten=new Intent(MainActivity.this,PersonalSpace.class);
                        startActivity(inten);
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_check:
                        Intent in=new Intent(MainActivity.this,CheckPlan.class);
                        startActivity(in);
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_chart:
                        Intent i=new Intent(MainActivity.this,PlanChart.class);
                        startActivity(i);
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_set:
                        Intent it=new Intent(MainActivity.this,PersonalSetings.class);
                        startActivity(it);
                        mDrawerLayout.closeDrawers();
                        break;
                }
                return true;
            }
        });
/////////////////////////////////////////////////////////////////////////////////////////	测试数据是否存在的
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
        listViewAdapter = new projectAdapter(listitems, this);
        list.setAdapter(listViewAdapter);
        this.registerForContextMenu(list);
/////////////////////////////////////////////////////////////////////////////////   触发listview点击事件
        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                getListID = listitems.get(arg2).get("ID");
                mposition = arg2;
                SWITCH = "om";
            }
        });
/////////////////////////////////////////////////////////////////////////////   触发按键点击事件 新建计划
        InitViews();
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
                map.put("ID", "" + cursor.getString(0));                                      //放ID
                map.put("content", "" + cursor.getString(1));         //放优先级和主题
                map.put("date&time", "" + cursor.getString(2) + "," + cursor.getString(3));              //放日期时间
                map.put("finished", cursor.getString(3));
                map.put("Date", cursor.getString(2));//放状态
                listItems.add(map);
            }
            cursor.moveToNext();
        }
        cursor.moveToFirst();
        return listItems;
    }
    public class projectAdapter extends BaseAdapter {

        private List<Map<String, String>> listItems;                                //接受参数数组
        Context context;

        public final class ListItemView                 //设置listview的内容和格式
        {
            public TextView content;
            public TextView datetime;
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
            ListItemView listItemView = null;
            if (convertView == null) {
                listItemView = new ListItemView();
                convertView = listContainer.inflate(R.layout.mylistitems, null);                         //设置view布局
                listItemView.content = (TextView) convertView.findViewById(R.id.Topic); //获得对象  记得加上布局名字后再加findviewbyid
                listItemView.datetime = (TextView) convertView.findViewById(R.id.ItemDateTime);
                convertView.setTag(listItemView);                                              //设置空间集到convertView
            } else {
                listItemView = (ListItemView) convertView.getTag();
            }
                listItemView.content.setText((String) listitems.get(position).get("content"));      //设置listview的内容
                listItemView.datetime.setText((String) listitems.get(position).get("date&time"));
                getID(selectID);
                return convertView;
            }
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        Log.v("test", "populate context menu");
        menu.setHeaderTitle("选择");                              // 设定 menu 标题
        menu.add(0, 2, Menu.NONE, "删除");
        menu.add(0, 3, Menu.NONE, "修改");
    }
    //////////////////////////////////////////////////////////////////响应上下文菜单项
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // 得到当前被选中的item信息
        super.onContextItemSelected(item);
        AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();//获得AdapterContextMenuInfo,以此来获得选择的listview项
        ItemID = listitems.get(menuInfo.position).get("ID");
        getPosition = menuInfo.position;
        SWITCH = "cm";
        switch (item.getItemId()) {                    //点击不同项 所触发的不同事件
            case 2:
                delete(ItemID);                             //删除
                break;
            case 3:
                edit(ItemID);                                 //编辑
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
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }

        return true;

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void edit(String ItemID)                                 //实现编辑事件
    {
        Intent intentToEdit = new Intent(MainActivity.this, editPage.class);
        Bundle sentId = new Bundle();
        sentId.putString("ID", ItemID);
        intentToEdit.putExtras(sentId);
        startActivity(intentToEdit);
    }
    public void delete(String getListID)                                //实现删除事件
    {
        final String getlistid = getListID;
        AlertDialog.Builder cancelConfirm = new AlertDialog.Builder(MainActivity.this);
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
                            Intent inten = new Intent(MainActivity.this, Alarmreceiver.class);
                            PendingIntent pii = PendingIntent.getBroadcast(MainActivity.this, 0, inten, 0);
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
    public void InitViews() {
        Intent i = new Intent(MainActivity.this, MyService.class);
        startService(i);
    }
}
