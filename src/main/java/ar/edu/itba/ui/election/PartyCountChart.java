package ar.edu.itba.ui.election;

import ar.edu.itba.model.config.Configuration;
import ar.edu.itba.model.handlers.Profiler;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PartyCountChart extends BaseChart {

    public PartyCountChart(final String title) {
        super(title);
    }

    public ChartPanel generateChartPanel() throws Exception {
        final CategoryDataset dataset = createDataset();
        chart = createChart(dataset);
        return generateChartPanel(false);
    }

    @Override
    protected CategoryDataset createDataset() throws Exception {
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        final Map<String, Long> partyVoters = Profiler.getPersons().stream().map(p -> p.getPoliticalParty())
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        final Map<String, Long> normalizedPartyVoters = normalize(partyVoters);

        for (final String party : normalizedPartyVoters.keySet())
            dataset.addValue(normalizedPartyVoters.get(party), "", party);
        return dataset;
    }

    private Map<String, Long> normalize(final Map<String, Long> partyVoters) throws Exception {
        final Map<String, Long> normalizedVoters = new HashMap<>();

        for (final String party : Configuration.getInstance().getPoliticalParties())
            normalizedVoters.put(party, 0L);

        for (final Map.Entry<String, Long> e : partyVoters.entrySet())
            normalizedVoters.put(e.getKey(), e.getValue());

        return normalizedVoters;
    }

    @Override
    protected JFreeChart createChart(final CategoryDataset dataset) throws Exception {
        ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
        BarRenderer.setDefaultBarPainter(new StandardBarPainter());
        final JFreeChart chart = ChartFactory.createBarChart(title,
                                    "Party",
                                    "Count",
                                    dataset,
                                    PlotOrientation.VERTICAL,
                                    false,
                                    false,
                                    false
                                );

        chart.setBackgroundPaint(new Color(249, 231, 236));

        final BarRenderer br = (BarRenderer) chart.getCategoryPlot().getRenderer();
        final Color barsColor = new Color(140, 140, 140);

        for (int i = 0 ; i < Configuration.getInstance().getPoliticalParties().size() ; i++)
            br.setSeriesPaint(i++, barsColor);
        return chart;
    }

    public void setChartHeight(final double maxValue) {
        final CategoryPlot plot = chart.getCategoryPlot();
        final ValueAxis yAxis = plot.getRangeAxis();
        yAxis.setRange(0D, maxValue);
    }
}

