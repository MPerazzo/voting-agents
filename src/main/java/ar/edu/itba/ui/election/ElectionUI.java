package ar.edu.itba.ui.election;

import ar.edu.itba.model.Person;
import ar.edu.itba.model.enums.SocialClass;
import ar.edu.itba.model.handlers.Profiler;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.ui.RefineryUtilities;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ElectionUI extends JFrame {

    private static Map<SocialClass, Long> previousCount = new HashMap<>();

    private static int electionCount = 1;

    private static double scoreChartUpperBoundMax = 0;
    private static double partyCountChartUpperBoundMax = 0;

    private NewsPaperPartyScoreChart newsPaperPartyScoreChart;
    private PartyCountChart partyCountChart;
    private EconomicClassChart economicClassChart;
    private EconomicActionScoreChart economicActionScoreChart;

    public ElectionUI() {
        super("Election " + electionCount + " - Details");
    }

    public void compute() throws Exception {
        newsPaperPartyScoreChart = new NewsPaperPartyScoreChart("Election " + electionCount + " - score by newspaper and party");
        partyCountChart = new PartyCountChart("Election " + electionCount + " - party voters");
        economicClassChart = new EconomicClassChart("Election " + electionCount + " - voters economic transition");
        economicActionScoreChart = new EconomicActionScoreChart("Election " + electionCount + " - economic actions " );

        this.setLayout(new FlowLayout());
        this.getContentPane().add(economicActionScoreChart.generateChartPanel());
        this.getContentPane().add(economicClassChart.generateChartPanel());
        this.getContentPane().add(partyCountChart.generateChartPanel());
        this.getContentPane().add(newsPaperPartyScoreChart.generateChartPanel());
        this.pack();

        updateBounds();

        setPreviousCount(Profiler.getInstance().getPersons());

        electionCount++;
    }

    private void updateBounds() {
        double height = newsPaperPartyScoreChart.getChartHeight();
        if (height > scoreChartUpperBoundMax)
            scoreChartUpperBoundMax = height;

        height = partyCountChart.getChartHeight();
        if (height > partyCountChartUpperBoundMax)
            partyCountChartUpperBoundMax = height;

        height = getElectionChartHeight();
        if (height > scoreChartUpperBoundMax)
            scoreChartUpperBoundMax = height;
    }

    public void showOnScreen() {
        newsPaperPartyScoreChart.setChartHeight(scoreChartUpperBoundMax);
        partyCountChart.setChartHeight(partyCountChartUpperBoundMax);
        setElectionChartBounds();
        ((BarRenderer) economicActionScoreChart.chart.getCategoryPlot().getRenderer()).setMaximumBarWidth(.25);

        RefineryUtilities.centerFrameOnScreen(this);
        this.setVisible(true);
    }

    private void setElectionChartBounds() {
        double economicActionChartUpperBound = economicActionScoreChart.chart.getCategoryPlot().getRangeAxis().getUpperBound();
        economicActionScoreChart.chart.getCategoryPlot().getRangeAxis().setUpperBound(
                economicActionChartUpperBound + scoreChartUpperBoundMax - getElectionChartHeight());
    }

    private double getElectionChartHeight() {
        return Math.abs(economicActionScoreChart.chart.getCategoryPlot().getRangeAxis().getUpperBound()) +
                Math.abs(economicActionScoreChart.chart.getCategoryPlot().getRangeAxis().getLowerBound());
    }

    public static void setPreviousCount(final List<Person> persons) {
        previousCount = persons.stream().collect(Collectors.groupingBy(Person::getSocialClass, Collectors.counting()));
    }

    public static Map<SocialClass, Long> getPreviousCount() {
        return previousCount;
    }
}
