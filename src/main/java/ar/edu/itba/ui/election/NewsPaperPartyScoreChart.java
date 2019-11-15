package ar.edu.itba.ui.election;

import ar.edu.itba.model.News;
import ar.edu.itba.model.config.Configuration;
import ar.edu.itba.model.handlers.Media;
import ar.edu.itba.model.handlers.NewsPaper;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NewsPaperPartyScoreChart extends BaseStackedChart {

    public NewsPaperPartyScoreChart(final String title) {
        super(title);
    }

    protected CategoryDataset createDataset() throws Exception {
        final int politicalParties = Configuration.getInstance().getPoliticalParties().size();
        final List<NewsPaper> newsPapers = Media.getInstance().getSources();
        final double[][] data = new double[6][politicalParties];

        int i = 0;
        for (final NewsPaper newsPaper : newsPapers) {
            final Map<String, Double> partiesRealImpact = newsPaper.getNews().stream().
                    collect(Collectors.toMap(News::getParty, News::getRealImpact, (v1, v2) -> v1 + v2));
            normalizeDoubleMap(partiesRealImpact);

            final Map<String, Double> partiesImpactDifferential = newsPaper.getNews().stream().
                    collect(Collectors.toMap(News::getParty, News::getImpactDifference, (v1, v2) -> v1 + v2));
            normalizeDoubleMap(partiesRealImpact);

            data[i++] = partiesRealImpact.entrySet().stream().sorted(Map.Entry.comparingByKey()).mapToDouble(e -> e.getValue()).toArray();
            data[i++] = partiesImpactDifferential.entrySet().stream().sorted(Map.Entry.comparingByKey()).mapToDouble(e -> e.getValue()).toArray();
        }

        String[] politicalPartiesId = Configuration.getInstance().getPoliticalParties().stream().sorted()
                .toArray(String[]::new);
        String[] newsPapersId = {"potentiel r", "potentiel t", "palmares r", "palmares t", "avenir r", "avenir t"};
        //String[] newsPapersId = newsPapers.stream().map(newsPaper -> newsPaper.getName()).toArray(String[]::new);
        return DatasetUtilities.createCategoryDataset(newsPapersId, politicalPartiesId, data);
    }

    protected JFreeChart createChart(final CategoryDataset dataset) {
        return super.createChart(dataset, title, "Party", "Score");
    }

    protected List<Color> generateColors() {
        final List<Color> colors = new LinkedList<>();
        colors.add(new Color(0, 0, 128));
        colors.add(new Color(0, 0, 255));
        colors.add(new Color(128, 0, 0));
        colors.add(new Color(255, 0, 0));
        colors.add(new Color(140, 140, 0));
        colors.add(new Color(255, 255, 0));
        return colors;
    }
}
