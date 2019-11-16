package ar.edu.itba.ui.election;

import ar.edu.itba.model.config.InitialConfiguration;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.HorizontalAlignment;

import java.awt.*;
import java.util.Map;

public abstract class BaseChart {

    protected String title;

    protected JFreeChart chart;

    public BaseChart(String title) {
        this.title = title;
    }

    public ChartPanel createChartPanel(final boolean legend) throws Exception {
        final CategoryDataset dataset = createDataset();
        chart = createChart(dataset);
        return generateChartPanel(legend);
    }

    private ChartPanel generateChartPanel(boolean legend) {
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

    public void setChartHeight(final double maxValue) {
        chart.getCategoryPlot().getRangeAxis().setRange(0D, maxValue);
    }

    public double getChartHeight() {
        return chart.getCategoryPlot().getRangeAxis().getUpperBound();
    }

    protected void normalizeDoubleMap(final Map<String, Double> map) throws Exception {
        for (final String party : InitialConfiguration.getInstance().getPoliticalParties()) {
            if (!map.containsKey(party))
                map.put(party, 0D);
        }
    }

    protected void normalizeLongMap(final Map<String, Long> map) throws Exception {
        for (final String party : InitialConfiguration.getInstance().getPoliticalParties()) {
            if (!map.containsKey(party))
                map.put(party, 0L);
        }
    }

    protected abstract CategoryDataset createDataset() throws Exception;
    protected abstract JFreeChart createChart(final CategoryDataset dataset) throws Exception;
    public abstract ChartPanel generateChartPanel() throws Exception;
}
