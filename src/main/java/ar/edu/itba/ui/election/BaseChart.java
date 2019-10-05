package ar.edu.itba.ui.election;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.HorizontalAlignment;

import java.awt.*;

public abstract class BaseChart {

    protected String title;

    protected JFreeChart chart;

    public BaseChart(String title) {
        this.title = title;
    }

    public ChartPanel generateChartPanel(boolean legend) {
        Font tickFont = new Font("Dialog", Font.PLAIN, 13);
        Font labelFont = new Font("Dialog", Font.PLAIN, 15);
        final CategoryPlot categoryPlot = chart.getCategoryPlot();
        final CategoryAxis categoryAxis = categoryPlot.getDomainAxis();
        final ValueAxis valueAxis = categoryPlot.getRangeAxis();
        categoryAxis.setTickLabelFont(tickFont);
        valueAxis.setTickLabelFont(tickFont);
        categoryAxis.setLabelFont(labelFont);
        valueAxis.setLabelFont(labelFont);

        if (legend)
            chart.getLegend().setHorizontalAlignment(HorizontalAlignment.CENTER);

        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 350));
        return chartPanel;
    }

    protected abstract CategoryDataset createDataset() throws Exception;
    protected abstract JFreeChart createChart(final CategoryDataset dataset) throws Exception;
}
