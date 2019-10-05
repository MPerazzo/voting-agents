package ar.edu.itba.ui.election;

import ar.edu.itba.model.config.Configuration;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public abstract class BaseStackedChart extends BaseChart {
    private static final List<Color> colors = generateColors();

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
        colors.add(new Color(200, 200, 0));
        return colors;
    }
}
