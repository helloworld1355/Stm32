package com.example.demo.utils;

import com.example.demo.entity.Demo1;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;

import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class CharUtil extends JFrame {

    public CharUtil(String title) {
        super(title);
        // 创建折线图的数据集
        XYSeries series = new XYSeries("数据系列");
        series.add(1.0, 1.0);
        series.add(2.0, 4.0);
        series.add(3.0, 3.0);
        series.add(4.0, 5.0);
        series.add(5.0, 2.0);

        XYSeriesCollection dataset = new XYSeriesCollection(series);

        // 创建折线图
        JFreeChart chart = ChartFactory.createXYLineChart(
                "LV and Speed vs Time",   // 图表标题
                "Time",         // X轴标签
                "Lv and Speed",         // Y轴标签
                dataset,       // 数据集
                PlotOrientation.VERTICAL,  // 图表方向
                true,           // 是否包含图例
                true,           // 是否显示工具提示
                false           // 是否显示URL链接
        );

        // 定制图表样式
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDomainPannable(true);
        plot.setRangePannable(true);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinesVisible(true);

        // 创建渲染器并设置线条颜色
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, true);
        renderer.setSeriesPaint(0, Color.BLUE);
        plot.setRenderer(renderer);

        // 创建图表面板并添加到窗口中
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        setContentPane(chartPanel);

        // 添加窗口关闭事件
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CharUtil example = new CharUtil("折线图示例");
            example.pack();
            example.setLocationRelativeTo(null);
            example.setVisible(true);
        });
    }
}
