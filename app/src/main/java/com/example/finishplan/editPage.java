package com.example.finishplan;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class editPage extends Activity {

	/////////////////////////////////////////////////////////////////////////定义基本控件
	EditText showtopic,showtime,showdate;
	ArrayAdapter aa;
	Button choosedate,choosetime,save,cancel;
	Calendar c;
	/////////////////////////////////////////////////////////////////////////定义传递中间量
	String gettopic="",getdate="",gettime="";
	String itemID;
	/////////////////////////////////////////////////////////////////////////定义数据库
	dataBase data;
	Cursor cursor;
	private final static int DATE_DIALOG = 0;
	private final static int TIME_DIALOG = 1;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editpage);
		Bundle getid = getIntent().getExtras();
		itemID = getid.getString("ID");                           //获得ID
/////////////////////////////////////////////////////////////////////////测试一下的
		Log.d("whether ID has got", itemID);
		Log.d("whether ID has got", "itemID");
		data = new dataBase(this);
		cursor = data.select(itemID);
		cursor.moveToFirst();
		showEditContent();                               //展示布局
		while (!cursor.isAfterLast()) {
			int id = cursor.getInt(0);
			String str = Integer.toString(id);
			int e = str.compareTo(itemID);
			Log.d("whether ID has got", str);
			Log.d("whether ID has got", itemID);
			if (e == 0) {
				Log.d("判断", "成功");
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
					Intent inten = new Intent(this, Alarmreceiver.class);
					PendingIntent pii = PendingIntent.getBroadcast(this, 0, inten, 0);
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
		}
	////////////////////////////////////////////////////////////////////////////////初始化
	public void showEditContent()
	{
		showtopic=(EditText)findViewById(R.id.edittopic);
		showtime=(EditText)findViewById(R.id.edittime);
		showdate=(EditText)findViewById(R.id.editdate);
		choosedate=(Button)findViewById(R.id.choosedate);
		choosetime=(Button)findViewById(R.id.choosetime);
		save=(Button)findViewById(R.id.edit);
		////////////////////////////////////////////////////////////////////////////////开始获取内容展示在布局上
		showtopic.setText(cursor.getString(1));
		showdate.setText(cursor.getString(2));
		choosedate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(DATE_DIALOG);
			}
		});
		showtime.setText(cursor.getString(3));
		choosetime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(TIME_DIALOG);

			}
		});
		aa= ArrayAdapter.createFromResource(editPage.this,R.array.setlevel,android.R.layout.simple_gallery_item); //将可选内容与ArrayAdapter连接起来
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  //设置下拉列表的风格
		aa.notifyDataSetChanged();                      //通知spinner刷新数据
		////////////////////////////////////////////////////////////////////////////////取消修改事件
		cancel=(Button)findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder cancelConfirm=new AlertDialog.Builder(editPage.this);
				cancelConfirm.setTitle("还改么？还没保存呢");
				cancelConfirm.setPositiveButton("不改了", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent toboss=new Intent(editPage.this,MainActivity.class);
						startActivity(toboss);
					}
				});
				cancelConfirm.setNegativeButton("修改吧", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
				cancelConfirm.show();
			}
		});
		////////////////////////////////////////////////////////////////////////////////保存修改事件
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				gettopic=showtopic.getText().toString();
				getdate=showdate.getText().toString();                                    //date
				gettime=showtime.getText().toString();                                        //time
				if (TextUtils.isEmpty(gettopic)) {
					Toast.makeText(editPage.this,"请输入计划内容！",Toast.LENGTH_SHORT).show();
					return;
				}

				if (TextUtils.isEmpty(getdate)) {
					Toast.makeText(editPage.this,"请输入计划日期！",Toast.LENGTH_SHORT).show();
					return;
				}
				if (TextUtils.isEmpty(gettime)) {
					Toast.makeText(editPage.this,"请输入计划时间！",Toast.LENGTH_SHORT).show();
					return;
				}
				else {
					data.update(itemID, gettopic, getdate, gettime);
					Toast.makeText(getApplicationContext(), "已保存修改！", Toast.LENGTH_SHORT).show();
					Intent toboss = new Intent(editPage.this, MainActivity.class);
					finish();
					startActivity(toboss);
				}
			}
		});
	}
	//////////////////////////////////////////////////////////////////////////////重写消息框以获取日期时间的方法
	@Override
	protected Dialog onCreateDialog(int id) {                 //overwrite the oncreate method
		Dialog dialog = null;
		c = Calendar.getInstance();
		switch (id) {
			case DATE_DIALOG:
				dialog = new DatePickerDialog(
						this,
						new DatePickerDialog.OnDateSetListener() {
							public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
								showdate.setText( year + "年" + (month+1) + "月" + dayOfMonth + "日");
							}
						},
						c.get(Calendar.YEAR), // 传入年份
						c.get(Calendar.MONTH), // 传入月份
						c.get(Calendar.DAY_OF_MONTH) // 传入天数
				);

				dialog.setTitle(c.get(Calendar.YEAR)+","+c.get(Calendar.MONTH)+","+c.get(Calendar.DAY_OF_MONTH));
				break;
			case TIME_DIALOG:
				dialog=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						showtime.setText(hourOfDay+":"+minute);
					}
				}, c.get(Calendar.HOUR), c.get(Calendar.MINUTE), true);
				break;
		}
		return dialog;
	}
	///////////////////////////////////////////////////////////////////////////这段参考以下网址的
////http://www.cnblogs.com/jxgxy/archive/2012/08/23/2653404.html
	private long exitTime = 0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent home = new Intent(editPage.this, MainActivity.class);
			home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(home);
		}
		return true;
	}
}

