package ar.edu.itba.ui.election;

import ar.edu.itba.model.config.Configuration;
import ar.edu.itba.model.handlers.Media;
import ar.edu.itba.model.handlers.NewsPaper;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//cambio de score por partido segun el medio
public class NewsPaperPartyScoreChart extends BaseStackedChart {

    public ChartPanel generateChartPanel(final int electionCount) throws Exception {
        final CategoryDataset dataset = createDataset();
        final JFreeChart chart = createChart(dataset, electionCount);
        return generateChartPanel(chart, true);
    }

    protected CategoryDataset createDataset() throws Exception {
        final int politicalParties = Configuration.getInstance().getPoliticalParties().size();
        final List<NewsPaper> newsPapers = Media.getSources();
        final double[][] data = new double[newsPapers.size()][politicalParties];

        int i = 0;
        for (final NewsPaper newsPaper : newsPapers) {
            final Map<String, Double> partiesImpactDifferential = newsPaper.getNews().stream().map(n -> n.getImpactDifferential()).flatMap(m -> m.entrySet().stream())
                    .collect(Collectors.toMap(Map.Entry::getKey,
                    Map.Entry::getValue,
                    (v1,v2) -> v1 + v2));
            final Map<String, Double> normalizedDifferential = normalize(partiesImpactDifferential);
            data[i++] = normalizedDifferential.values().stream().mapToDouble(v -> v).toArray();
        }

        String[] politicalPartiesId = Configuration.getInstance().getPoliticalParties().stream().toArray(String[]::new);
        String[] newsPapersId = newsPapers.stream().map(newsPaper -> newsPaper.getId()).toArray(String[]::new);
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

    protected JFreeChart createChart(final CategoryDataset dataset, final int electionCount) throws Exception {
        return super.createChart(dataset, "Election " + electionCount + " - score by newspaper and party",
                "Party", "Score");
    }
}
