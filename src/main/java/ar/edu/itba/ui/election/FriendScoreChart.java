package ar.edu.itba.ui.election;

import ar.edu.itba.model.handlers.Friendship;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.util.Map;
import java.util.stream.Collectors;

public class FriendScoreChart extends BaseFlatChart {

    public FriendScoreChart(final String title) {
        super(title);
    }

    @Override
    protected CategoryDataset createDataset() throws Exception {
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        final Map<String, Double> partyScore = Friendship.getFriendshipScores();
        normalizeDoubleMap(partyScore);

        for (final String party : partyScore.keySet().stream().sorted().collect(Collectors.toList()))
            dataset.addValue(partyScore.get(party), "", party);
        return dataset;
    }

    @Override
    protected JFreeChart createChart(CategoryDataset dataset) throws Exception {
        return createChart(dataset, "Party", "Score", new Color(0, 143, 122));
    }
}
