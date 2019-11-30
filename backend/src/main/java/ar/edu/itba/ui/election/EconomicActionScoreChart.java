package ar.edu.itba.ui.election;

import ar.edu.itba.model.EconomicAction;
import ar.edu.itba.model.Election;
import ar.edu.itba.model.enums.SocialClass;
import ar.edu.itba.model.handlers.EconomicMinistry;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EconomicActionScoreChart extends BaseStackedChart {

    public EconomicActionScoreChart(String title) {
        super(title);
    }

    protected CategoryDataset createDataset() {
        final List<EconomicAction> economicActions = EconomicMinistry.getActions();
        final double[][] data = new double[3][1];

        final Map<String, Double> totalImpact = new HashMap<>();
        for (SocialClass s : SocialClass.values())
            totalImpact.put(s.getName(), 0D);

        for (final EconomicAction e : economicActions) {
            for (Map.Entry<SocialClass, Double> entry : e.getTotalImpact().entrySet()) {
                final String socialClass = entry.getKey().getName();
                totalImpact.put(socialClass, totalImpact.get(socialClass) + entry.getValue());
            }
        }

        String[] ruler = new String[1];
        ruler[0] = Election.getInstance().getPrevRuler();
        String[] economicClass = {"high","mid","low"};

        data[0][0] = totalImpact.get("high");
        data[1][0] = totalImpact.get("mid");
        data[2][0] = totalImpact.get("low");

        return DatasetUtilities.createCategoryDataset(economicClass, ruler, data);
    }

    protected JFreeChart createChart(final CategoryDataset dataset) {
        return super.createChart(dataset, title, "Party", "Score");
    }

    protected List<Color> generateColors() {
        final List<Color> colors = new LinkedList<>();
        colors.add(new Color(124, 10, 2));
        colors.add(new Color(178, 34, 34));
        colors.add(new Color(226, 88, 34));
        return colors;
    }
}
