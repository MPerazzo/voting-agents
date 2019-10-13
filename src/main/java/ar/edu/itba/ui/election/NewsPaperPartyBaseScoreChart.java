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

    protected CategoryDataset createDataset(final Function<News, Double> f) throws Exception {
        final int politicalParties = Configuration.getInstance().getPoliticalParties().size();
        final List<NewsPaper> newsPapers = Media.getInstance().getSources();
        final double[][] data = new double[newsPapers.size()][politicalParties];

        int i = 0;
        for (final NewsPaper newsPaper : newsPapers) {
            final Map<String, Double> partiesImpactDifferential = newsPaper.getNews().stream().
                    collect(Collectors.toMap(News::getParty, f, (v1, v2) -> v1 + v2));
            normalizeDoubleMap(partiesImpactDifferential);
            data[i++] = partiesImpactDifferential.entrySet().stream().sorted(Map.Entry.comparingByKey()).mapToDouble(e -> e.getValue()).toArray();
        }

        String[] politicalPartiesId = Configuration.getInstance().getPoliticalParties().stream().sorted()
                .toArray(String[]::new);
        String[] newsPapersId = newsPapers.stream().map(newsPaper -> newsPaper.getName()).toArray(String[]::new);
        return DatasetUtilities.createCategoryDataset(newsPapersId, politicalPartiesId, data);
    }

    protected JFreeChart createChart(final CategoryDataset dataset) throws Exception {
        return super.createChart(dataset, title, "Party", "Score");
    }
}
