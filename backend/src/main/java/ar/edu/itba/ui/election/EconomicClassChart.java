package ar.edu.itba.ui.election;

import ar.edu.itba.model.Person;
import ar.edu.itba.model.enums.SocialClass;
import ar.edu.itba.model.handlers.Profiler;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class EconomicClassChart extends BaseFlatChart {

    public EconomicClassChart(final String title) {
        super(title);
    }

    protected CategoryDataset createDataset() {

        final Map<SocialClass, Long> newCount = ElectionUI.calculateEconomicTransition(Profiler.getInstance().getPersons());
        final Map<SocialClass, Long> oldCount = ElectionUI.getPreviousCount();

        final Map<SocialClass, Long> differential = new HashMap<>();
        differential.put(SocialClass.LOW, newCount.get(SocialClass.LOW) - oldCount.get(SocialClass.LOW));
        differential.put(SocialClass.MID, newCount.get(SocialClass.MID) - oldCount.get(SocialClass.MID));
        differential.put(SocialClass.HIGH, newCount.get(SocialClass.HIGH) - oldCount.get(SocialClass.HIGH));

        ElectionUI.setPreviousCount(newCount);

        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(differential.get(SocialClass.LOW), "", SocialClass.LOW.getName());
        dataset.addValue(differential.get(SocialClass.MID), "", SocialClass.MID.getName());
        dataset.addValue(differential.get(SocialClass.HIGH), "", SocialClass.HIGH.getName());
        return dataset;
    }

    @Override
    protected JFreeChart createChart(CategoryDataset dataset) throws Exception {
        return createChart(dataset, "Party", "Voters", new Color(0, 143, 122));
    }
}
