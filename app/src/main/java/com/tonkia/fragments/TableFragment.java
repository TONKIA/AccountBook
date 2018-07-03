package com.tonkia.fragments;

import android.os.Bundle;

import android.support.v4.app.Fragment;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tonkia.R;
import com.tonkia.vo.DealItem;
import com.tonkia.vo.DealRecord;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.LineChartView;

public class TableFragment extends Fragment {

    private LineChartView weekChart;
    private LineChartView monthChart;
    private LineChartView yearChart;
    private int year;
    private int month;
    private int week;
    private int dayOfMonth;

    private String[] xTitle = new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_table, container, false);
        weekChart = view.findViewById(R.id.week_chart);
        monthChart = view.findViewById(R.id.month_chart);
        yearChart = view.findViewById(R.id.year_chart);

        freshChart();
        return view;
    }

    public void freshChart() {
        initTime();
        initWeekChart();
        initMonthChart();
        initYearChart();
    }


    private void initTime() {
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        week = c.get(Calendar.WEEK_OF_YEAR);
        dayOfMonth = c.getActualMaximum(Calendar.DATE);
    }

    public void initWeekChart() {

        //X轴
        List<AxisValue> xList = new LinkedList<>();
        //设置坐标点
        List<PointValue> values = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            List<DealRecord> list = LitePal.where("year=? and week=? and type=? and dayOfWeek=?", "" + year, "" + week, DealItem.OUTPUT + "", "" + (i + 1)).find(DealRecord.class);
            float value = 0;
            for (DealRecord dr : list) {
                value += dr.getCost();
            }
            PointValue pv = new PointValue(i, value);
            if (value > 0)
                pv.setLabel("本" + xTitle[i] + "支出" + value + "元");
            else
                pv.setLabel("");
            values.add(pv);
            AxisValue av = new AxisValue(i);
            av.setLabel(xTitle[i]);
            xList.add(av);
        }


        //创建折线
        Line line = new Line(values).setColor(ContextCompat.getColor(getContext(), R.color.lineColor)).setCubic(false);
        line.setShape(ValueShape.CIRCLE);
        line.setStrokeWidth(1);
        line.setPointRadius(2);
        line.setFilled(true);
        line.setPointColor(ContextCompat.getColor(getContext(), R.color.pointColor));
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
        line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);

        //创建折线表
        List<Line> lines = new ArrayList<>();
        lines.add(line);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));  //设置字体颜色
        axisX.setHasLines(true); //x 轴分割线
        axisX.setTextSize(10);//设置字体大小
        axisX.setValues(xList);  //填充X轴的坐标名称

        //创建直线数据
        LineChartData data = new LineChartData();
        data.setLines(lines);
        data.setAxisXBottom(axisX);

        weekChart.setLineChartData(data);
        weekChart.setInteractive(true);
        weekChart.setZoomEnabled(false);
        weekChart.setValueSelectionEnabled(true);
    }


    private void initMonthChart() {

        //X轴
        List<AxisValue> xList = new LinkedList<>();
        //设置坐标点
        List<PointValue> values = new ArrayList<>();

        for (int i = 0; i < dayOfMonth; i++) {
            List<DealRecord> list = LitePal.where("year=? and month=? and type=? and dayOfMonth=?", "" + year, "" + month, DealItem.OUTPUT + "", (i + 1) + "").find(DealRecord.class);
            float value = 0;
            for (DealRecord dr : list) {
                value += dr.getCost();
            }
            PointValue pv = new PointValue(i, value);
            if (value > 0)
                pv.setLabel("本月" + ((i + 1) < 10 ? "0" + (i + 1) : (i + 1)) + "号支出" + value + "元");
            else
                pv.setLabel("");
            values.add(pv);
            AxisValue av = new AxisValue(i);
            av.setLabel((i + 1) < 10 ? "0" + (i + 1) : "" + (i + 1));
            xList.add(av);
        }


        //创建折线
        Line line = new Line(values).setColor(ContextCompat.getColor(getContext(), R.color.lineColor)).setCubic(false);
        line.setShape(ValueShape.CIRCLE);
        line.setStrokeWidth(1);
        line.setPointRadius(2);
        line.setFilled(true);
        line.setPointColor(ContextCompat.getColor(getContext(), R.color.pointColor));
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
        line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);

        //创建折线表
        List<Line> lines = new ArrayList<>();
        lines.add(line);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(false);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));  //设置字体颜色
        axisX.setHasLines(true); //x 轴分割线
        axisX.setTextSize(10);//设置字体大小
        axisX.setValues(xList);  //填充X轴的坐标名称

        //创建直线数据
        LineChartData data = new LineChartData();
        data.setLines(lines);
        data.setAxisXBottom(axisX);

        monthChart.setLineChartData(data);
        monthChart.setInteractive(true);
        monthChart.setZoomEnabled(true);
        monthChart.setZoomType(ZoomType.HORIZONTAL);
        monthChart.setMaxZoom(3.5f);
        monthChart.setValueSelectionEnabled(true);
    }

    private void initYearChart() {


        //X轴
        List<AxisValue> xList = new LinkedList<>();
        //设置坐标点
        List<PointValue> values = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            List<DealRecord> list = LitePal.where("year=? and type=? and month=?", "" + year, DealItem.OUTPUT + "", i + "").find(DealRecord.class);

            float value = 0;
            for (DealRecord dr : list) {
                value += dr.getCost();
            }
            PointValue pv = new PointValue(i, value);
            if (value > 0)
                pv.setLabel(((i + 1) < 10 ? "0" + (i + 1) : (i + 1)) + "月共支出" + value + "元");
            else
                pv.setLabel("");
            values.add(pv);
            AxisValue av = new AxisValue(i);
            av.setLabel((i + 1) < 10 ? "0" + (i + 1) : "" + (i + 1));
            xList.add(av);
        }


        //创建折线
        Line line = new Line(values).setColor(ContextCompat.getColor(getContext(), R.color.lineColor)).setCubic(false);
        line.setShape(ValueShape.CIRCLE);
        line.setStrokeWidth(1);
        line.setPointRadius(2);
        line.setFilled(true);
        line.setPointColor(ContextCompat.getColor(getContext(), R.color.pointColor));
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
        line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);

        //创建折线表
        List<Line> lines = new ArrayList<>();
        lines.add(line);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(false);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));  //设置字体颜色
        axisX.setHasLines(true); //x 轴分割线
        axisX.setTextSize(10);//设置字体大小
        axisX.setValues(xList);  //填充X轴的坐标名称

        //创建直线数据
        LineChartData data = new LineChartData();
        data.setLines(lines);
        data.setAxisXBottom(axisX);

        yearChart.setLineChartData(data);
        yearChart.setInteractive(true);
        yearChart.setZoomEnabled(true);
        yearChart.setZoomType(ZoomType.HORIZONTAL);
        yearChart.setMaxZoom(1.5f);
        yearChart.setValueSelectionEnabled(true);
    }


}
