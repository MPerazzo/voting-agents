package ar.edu.itba.ui.election;

import ar.edu.itba.model.config.Configuration;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.HorizontalAlignment;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public abstract class BaseStackedChart extends BaseChart {
    private static final List<Color> colors = generateColors();

    public ChartPanel generateChartPanel(final JFreeChart chart) {

        Font tickFont = new Font("Dialog", Font.PLAIN, 13);
        Font labelFont = new Font("Dialog", Font.PLAIN, 15);
        final CategoryPlot categoryPlot = chart.getCategoryPlot();
        final CategoryAxis categoryAxis = categoryPlot.getDomainAxis();
        final ValueAxis valueAxis = categoryPlot.getRangeAxis();
        categoryAxis.setTickLabelFont(tickFont);
        valueAxis.setTickLabelFont(tickFont);
        categoryAxis.setLabelFont(labelFont);
        valueAxis.setLabelFont(labelFont);

        chart.getLegend().setHorizontalAlignment(HorizontalAlignment.CENTER);

        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 350));
        return chartPanel;
    }

    protected JFreeChart createChart(final CategoryDataset dataset, final String title, final String labelX,
                                     final String labelY) throws Exception {

        //Default theme (bar colors, etc)
        ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
        BarRenderer.setDefaultBarPainter(new StandardBarPainter());

        final JFreeChart chart = ChartFactory.createStackedBarChart(
                title, labelX, labelY,
                dataset, PlotOrientation.VERTICAL, true, true, false);

        chart.setBackgroundPaint(new Color(249, 231, 236));

        CategoryPlot plot = chart.getCategoryPlot();

        for (int i = 0; i < Configuration.getInstance().getPoliticalParties().size() ; i++)
            plot.getRenderer().setSeriesPaint(i, colors.get(i));

        return chart;
    }

    private static java.util.List<Color> generateColors() {
        final List<Color> colors = new LinkedList<>();
        colors.add(new Color(0, 0, 128));
        colors.add(new Color(128, 0, 0));
        colors.add(new Color(200, 160, 0));
        return colors;
    }
}
