package com.example.finishplan;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * 使用HelloCharts图表开发库实现折线图
 * Created by Administrator on 2016/10/26.
 */
public class PlanChart extends Activity{

    LineChartView lineChartView;
    Cursor cursor;                          //定义指向数据的游标
    dataBase data;
    String[] date = {"10-22","11-22","12-22","1-22","6-22","5-23","5-22"};//X轴的标注
    int[] score=new int[7];
    int a,s,d,f,g,h,j;
    int z,x,c,v,b,n,m;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_chart);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        int year=calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        date[6] = month + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日";
        String date6=year+"年"+date[6];
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        int month2 = calendar.get(Calendar.MONTH) + 1;
        date[5] = month2 + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日";
        String date5=year+"年"+date[5];
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        int month3 = calendar.get(Calendar.MONTH) + 1;
        date[4] = month3 + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日";
        String date4=year+"年"+date[4];
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        int month4 = calendar.get(Calendar.MONTH) + 1;
        date[3] = month4 + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日";
        String date3=year+"年"+date[3];
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        int month5 = calendar.get(Calendar.MONTH) + 1;
        date[2] = month5 + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日";
        String date2=year+"年"+date[2];
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        int month6 = calendar.get(Calendar.MONTH) + 1;
        date[1] = month6 + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日";
        String date1=year+"年"+date[1];
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        int month7 = calendar.get(Calendar.MONTH) + 1;
        date[0] = month7 + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日";
        String date0=year+"年"+date[0];
        data = new dataBase(this);
        cursor = data.select(1);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {

            String Date = cursor.getString(2);
            String state=cursor.getString(4);
            if(Date.compareTo(date0)==0){
                a++;
                if(state.compareTo("已完成!")==0){
                    z++;
                }
            }
            if(Date.compareTo(date1)==0){
                s++;
                if(state.compareTo("已完成!")==0){
                    x++;
                }
            }
            if(Date.compareTo(date2)==0){
                d++;
                if(state.compareTo("已完成!")==0){
                    c++;
                }
            }
            if(Date.compareTo(date3)==0){
                f++;
                if(state.compareTo("已完成!")==0){
                    v++;
                }
            }
            if(Date.compareTo(date4)==0){
                g++;
                if(state.compareTo("已完成!")==0){
                    b++;
                }
            }
            if(Date.compareTo(date5)==0){
                h++;
                if(state.compareTo("已完成!")==0){
                    n++;
                }
            }
            if(Date.compareTo(date6)==0){
                j++;
                if(state.compareTo("已完成!")==0){
                    m++;
                }
            }
            cursor.moveToNext();
        }
//        if(a==0){score[0]=;}
//        if(s==0){score[1]=0;}
//        if(d==0){score[2]=0;}
//        if(f==0){score[3]=0;}
//        if(g==0){score[4]=0;}
//        if(h==0){score[5]=0;}
//        if(j==0){score[6]=0;}
        if(a!=0){score[0]=100*z/a;}
        if(s!=0){score[1]=100*x/s;}
        if(d!=0){score[2]=100*c/d;}
        if(f!=0){score[3]=100*v/f;}
        if(g!=0){score[4]=100*b/g;}
        if(h!=0){score[5]=100*n/h;}
        if(j!=0){score[6]=100*m/j;}
        setView();
        initView();
    }
    private void setView(){
        lineChartView = (LineChartView)findViewById(R.id.activity_charts_line_lcv);
    }
    private void initView(){
        List<PointValue> mPointValues = new ArrayList<PointValue>();
        List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
        Axis axisY = new Axis();  //Y轴
        for (int i = 0; i < date.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(date[i]));
        }
        for (int i = 0; i < score.length; i++) {
            mPointValues.add(new PointValue(i, score[i]));
        }
        Line line = new Line(mPointValues).setColor(Color.BLUE).setCubic(true);
        List<Line> lines = new ArrayList<Line>();
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.GREEN);  //设置字体颜色
        axisX.setName("计划时间");  //表格名称
        axisX.setTextSize(12);//设置字体大小
        axisX.setMaxLabelChars(7); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        //Y轴
        axisY.setName("完成百分比");//y轴标注
        axisY.setTextSize(12);//设置字体大小
        axisY.setTextSize(12);//设置字体大小
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        axisY.setTextColor(Color.RED);
        //设置行为属性，支持缩放、滑动以及平移
        lineChartView.setInteractive(true);
        lineChartView.setZoomType(ZoomType.HORIZONTAL);
        lineChartView.setMaxZoom((float) 2);//最大方法比例
        lineChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChartView.setLineChartData(data);
        lineChartView.setVisibility(View.VISIBLE);
        axisX.setHasLines(true); //x 轴分割线
        lineChartView.setLineChartData(data);
    }
}
