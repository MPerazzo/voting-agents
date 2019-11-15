package ar.edu.itba.ui.election;

import ar.edu.itba.model.News;
import ar.edu.itba.model.Person;
import ar.edu.itba.model.config.Configuration;
import ar.edu.itba.model.enums.SocialClass;
import ar.edu.itba.model.handlers.Media;
import ar.edu.itba.model.handlers.NewsPaper;
import ar.edu.itba.model.handlers.Profiler;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DatasetUtilities;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class EconomicClassChart extends BaseFlatChart {

    public EconomicClassChart(final String title) {
        super(title);
    }

    protected CategoryDataset createDataset() {

        final Map<SocialClass, Long> newCount = Profiler.getInstance().getPersons().stream().collect(Collectors.groupingBy(Person::getSocialClass,
                Collectors.counting()));
        final Map<SocialClass, Long> oldCount = ElectionUI.getPreviousCount();

        final Map<SocialClass, Long> differential = new HashMap<>();
        differential.put(SocialClass.LOW, newCount.get(SocialClass.LOW) - oldCount.get(SocialClass.LOW));
        differential.put(SocialClass.MID, newCount.get(SocialClass.MID) - oldCount.get(SocialClass.MID));
        differential.put(SocialClass.HIGH, newCount.get(SocialClass.HIGH) - oldCount.get(SocialClass.HIGH));

        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(differential.get(SocialClass.LOW), "", SocialClass.LOW.getName());
        dataset.addValue(differential.get(SocialClass.MID), "", SocialClass.MID.getName());
        dataset.addValue(differential.get(SocialClass.HIGH), "", SocialClass.HIGH.getName());
        return dataset;
    }

    @Override
    protected JFreeChart createChart(CategoryDataset dataset) throws Exception {
        return createChart(dataset, "Party", "Score", new Color(0, 143, 122));
    }
}
