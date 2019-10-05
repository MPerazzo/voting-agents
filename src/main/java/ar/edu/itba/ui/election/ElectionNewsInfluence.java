package ar.edu.itba.ui.election;

import ar.edu.itba.model.handlers.Profiler;
import org.jfree.ui.RefineryUtilities;
import javax.swing.*;
import java.awt.*;

public class ElectionNewsInfluence extends JFrame {

    private static int electionCount = 1;

    private static double newsPartyScoreChartUpperBoundMax = 0;
    private static double newsPartyRealScoreChartUpperBoundMax = 0;

    private NewsPaperPartyScoreChart newsPaperPartyScoreChart;
    private NewsPaperPartyRealScoreChart newsPaperPartyRealScoreChart;
    private PartyCountChart partyCountChart;

    public ElectionNewsInfluence() {
        super("Election " + electionCount + " - News Influence");
    }

    public void compute() throws Exception {
        newsPaperPartyScoreChart = new NewsPaperPartyScoreChart("Election " + electionCount + " - total score by newspaper and party");
        newsPaperPartyRealScoreChart = new NewsPaperPartyRealScoreChart("Election " + electionCount + " - real score by newspaper and party");
        partyCountChart = new PartyCountChart("Election " + electionCount + " - News Influence");

        this.setLayout(new FlowLayout());
        this.getContentPane().add(newsPaperPartyScoreChart.generateChartPanel());
        this.getContentPane().add(newsPaperPartyRealScoreChart.generateChartPanel());
        this.getContentPane().add(partyCountChart.generateChartPanel());
        this.pack();

        updateBounds();

        electionCount++;
    }

    private void updateBounds() {
        double upperBound = newsPaperPartyScoreChart.getChartHeight();
        if (upperBound > newsPartyScoreChartUpperBoundMax)
            newsPartyScoreChartUpperBoundMax = upperBound;

        upperBound = newsPaperPartyRealScoreChart.getChartHeight();
        if (upperBound > newsPartyRealScoreChartUpperBoundMax)
            newsPartyRealScoreChartUpperBoundMax = upperBound;
    }

    public void showOnScreen() {
        newsPaperPartyScoreChart.setChartHeight(newsPartyScoreChartUpperBoundMax);
        newsPaperPartyRealScoreChart.setChartHeight(newsPartyRealScoreChartUpperBoundMax);
        partyCountChart.setChartHeight(Profiler.getPersons().size());
        RefineryUtilities.centerFrameOnScreen(this);
        this.setVisible(true);
    }
}
