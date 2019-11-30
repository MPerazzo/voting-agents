package ar.edu.itba.ui.election;

import ar.edu.itba.model.config.InitialConfiguration;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;

import java.awt.*;

public abstract class BaseFlatChart extends BaseChart {
    public BaseFlatChart(String title) {
        super(title);
    }

    @Override
    public ChartPanel generateChartPanel() throws Exception {
        return createChartPanel(false);
    }

    protected JFreeChart createChart(final CategoryDataset dataset, final String xLabel,
                                     final String yLabel, final Color barsColor) throws Exception {
        ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
        BarRenderer.setDefaultBarPainter(new StandardBarPainter());
        final JFreeChart chart = ChartFactory.createBarChart(title,
                xLabel,
                yLabel,
                dataset,
                PlotOrientation.VERTICAL,
                false,
                false,
                false
        );

        chart.setBackgroundPaint(new Color(249, 231, 236));

        final BarRenderer br = (BarRenderer) chart.getCategoryPlot().getRenderer();

        for (int i = 0; i < InitialConfiguration.getInstance().getPoliticalParties().size() ; i++)
            br.setSeriesPaint(i++, barsColor);
        return chart;
    }
}
