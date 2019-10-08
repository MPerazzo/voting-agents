package ar.edu.itba.ui.election;

import ar.edu.itba.model.handlers.Profiler;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PartyCountChart extends BaseFlatChart {

    public PartyCountChart(final String title) {
        super(title);
    }

    @Override
    protected CategoryDataset createDataset() throws Exception {
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        final Map<String, Long> partyVoters = Profiler.getPersons().stream().map(p -> p.getPoliticalParty())
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        normalizeLongMap(partyVoters);

        for (final String party : partyVoters.keySet().stream().sorted().collect(Collectors.toList()))
            dataset.addValue(partyVoters.get(party), "", party);
        return dataset;
    }

    @Override
    protected JFreeChart createChart(CategoryDataset dataset) throws Exception {
        return createChart(dataset, "Party", "Voters", new Color(140, 140, 140));
    }
}

