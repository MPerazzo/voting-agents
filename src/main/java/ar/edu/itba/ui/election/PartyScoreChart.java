package ar.edu.itba.ui.election;

import ar.edu.itba.model.config.Configuration;
import ar.edu.itba.model.handlers.Profiler;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.util.Map;
import java.util.stream.Collectors;

public class PartyScoreChart extends BaseChart{

    public ChartPanel generateChartPanel(int electionCount) throws Exception {
        final CategoryDataset dataset = createDataset();
        final JFreeChart chart = createChart(dataset, electionCount);
        return generateChartPanel(chart, false);
    }

    @Override
    protected CategoryDataset createDataset() throws Exception {
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        final Map<String, Double> partyScore = Profiler.getPersons().stream().map(p -> p.getPoliticalOrientation()).flatMap(m -> m.entrySet().stream())
        .collect(Collectors.toMap(Map.Entry::getKey,
                Map.Entry::getValue,
                (v1,v2) -> v1 + v2));
        for (final Map.Entry<String, Double> e : partyScore.entrySet())
            dataset.addValue(e.getValue(), "", e.getKey());
        return dataset;
    }

    @Override
    protected JFreeChart createChart(CategoryDataset dataset, int electionCount) throws Exception {
        ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
        BarRenderer.setDefaultBarPainter(new StandardBarPainter());
        final JFreeChart chart = ChartFactory.createBarChart("Election " + electionCount + " - total score by party",
                                    "Party",
                                    "Score",
                                    dataset,
                                    PlotOrientation.VERTICAL,
                                    false,
                                    false,
                                    false
                                );

        chart.setBackgroundPaint(new Color(249, 231, 236));

        final BarRenderer br = (BarRenderer) chart.getCategoryPlot().getRenderer();
        final Color barsColor = new Color(140, 140, 140);

        int i = 0;
        for (final String p : Configuration.getInstance().getPoliticalParties())
            br.setSeriesPaint(i++, barsColor);

        return chart;
    }
}

