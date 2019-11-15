package ar.edu.itba.ui.election;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;

import java.awt.*;
import java.util.List;

public abstract class BaseStackedChart extends BaseChart {
    private List<Color> colors = generateColors();

    public BaseStackedChart(final String title) {
        super(title);
    }

    @Override
    public ChartPanel generateChartPanel() throws Exception {
        return createChartPanel(true);
    }

    protected JFreeChart createChart(final CategoryDataset dataset, final String title, final String labelX,
                                     final String labelY) {

        //Default theme (bar colors, etc)
        ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
        BarRenderer.setDefaultBarPainter(new StandardBarPainter());

        final JFreeChart chart = ChartFactory.createStackedBarChart(
                title, labelX, labelY,
                dataset, PlotOrientation.VERTICAL, true, true, false);

        chart.setBackgroundPaint(new Color(249, 231, 236));

        CategoryPlot plot = chart.getCategoryPlot();

        for (int i = 0; i < dataset.getRowCount() ; i++)
            plot.getRenderer().setSeriesPaint(i, colors.get(i));

        return chart;
    }

    protected abstract List<Color> generateColors();
}
