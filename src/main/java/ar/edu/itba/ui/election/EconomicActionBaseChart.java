package ar.edu.itba.ui.election;

import ar.edu.itba.model.EconomicAction;
import ar.edu.itba.model.config.Configuration;
import ar.edu.itba.model.enums.SocialClass;
import ar.edu.itba.model.handlers.EconomicMinistry;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class EconomicActionBaseChart extends BaseStackedChart {

    public EconomicActionBaseChart(String title) {
        super(title);
    }

    //REFACTOR
    protected CategoryDataset createDataset() throws Exception {
        final int politicalParties = Configuration.getInstance().getPoliticalParties().size();
        final List<EconomicAction> economicActions = EconomicMinistry.getActions();
        final double[][] data = new double[3][politicalParties];

        for(EconomicAction e : economicActions){
            if(e.getRuler().equals("bakongo")) {
                data[0][0] = data[0][0] + e.getImpact().get(SocialClass.LOW);
                data[1][0] = data[1][0] + e.getImpact().get(SocialClass.MID);
                data[2][0] = data[2][0] + e.getImpact().get(SocialClass.HIGH);
            }
            if(e.getRuler().equals("lumumbista")) {
                data[0][1] = data[0][1] + e.getImpact().get(SocialClass.LOW);
                data[1][1] = data[1][1] + e.getImpact().get(SocialClass.MID);
                data[2][1] = data[2][1] + e.getImpact().get(SocialClass.HIGH);
            }
            if(e.getRuler().equals("simba")) {
                data[0][2] = data[0][2] + e.getImpact().get(SocialClass.LOW);
                data[1][2] = data[1][2] + e.getImpact().get(SocialClass.MID);
                data[2][2] = data[2][2] + e.getImpact().get(SocialClass.HIGH);
            }
        }

        String[] politicalPartiesId = Configuration.getInstance().getPoliticalParties().stream().sorted()
                .toArray(String[]::new);
        String[] economicClass = {"Low","Medium","High"};

        return DatasetUtilities.createCategoryDataset(economicClass, politicalPartiesId, data);
    }

    protected JFreeChart createChart(final CategoryDataset dataset) throws Exception {
        return super.createChart(dataset, title, "Party", "Score");
    }
}
