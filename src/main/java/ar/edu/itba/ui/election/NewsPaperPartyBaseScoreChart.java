package ar.edu.itba.ui.election;

import ar.edu.itba.model.News;
import ar.edu.itba.model.config.Configuration;
import ar.edu.itba.model.handlers.Media;
import ar.edu.itba.model.handlers.NewsPaper;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class NewsPaperPartyBaseScoreChart extends BaseStackedChart {

    public NewsPaperPartyBaseScoreChart(final String title) {
        super(title);
    }

    public ChartPanel generateChartPanel() throws Exception {
        final CategoryDataset dataset = createDataset();
        chart = createChart(dataset);
        return generateChartPanel(true);
    }

    protected CategoryDataset createDataset(final Function<News, Double> f) throws Exception {
        final int politicalParties = Configuration.getInstance().getPoliticalParties().size();
        final List<NewsPaper> newsPapers = Media.getSources();
        final double[][] data = new double[newsPapers.size()][politicalParties];

        int i = 0;
        for (final NewsPaper newsPaper : newsPapers) {
            final Map<String, Double> partiesImpactDifferential = newsPaper.getNews().stream().
                    collect(Collectors.toMap(News::getParty, f, (v1, v2) -> v1 + v2));
            final Map<String, Double> normalizedDifferential = normalize(partiesImpactDifferential);
            data[i++] = normalizedDifferential.values().stream().mapToDouble(v -> v).toArray();
        }

        String[] politicalPartiesId = Configuration.getInstance().getPoliticalParties().stream()
                .collect(Collectors.toSet()).toArray(String[]::new);
        String[] newsPapersId = newsPapers.stream().map(newsPaper -> newsPaper.getName()).toArray(String[]::new);
        return DatasetUtilities.createCategoryDataset(newsPapersId, politicalPartiesId, data);
    }

    private Map<String, Double> normalize(final Map<String, Double> partiesImpactDifferential) throws Exception {
        final Map<String, Double> normalizedDifferential = new HashMap<>();

        for (final String party : Configuration.getInstance().getPoliticalParties())
            normalizedDifferential.put(party, 0D);

        for (final Map.Entry<String, Double> e : partiesImpactDifferential.entrySet())
            normalizedDifferential.put(e.getKey(), e.getValue());

        return normalizedDifferential;
    }

    protected JFreeChart createChart(final CategoryDataset dataset) throws Exception {
        return super.createChart(dataset, title, "Party", "Score");
    }

    public double getChartHeight() {
        return chart.getCategoryPlot().getRangeAxis().getUpperBound();
    }

    public void setChartHeight(final double maxValue) {
        chart.getCategoryPlot().getRangeAxis().setRange(0D, maxValue);
    }
}
