package ar.edu.itba.ui.election;

import ar.edu.itba.model.handlers.Profiler;
import org.jfree.ui.RefineryUtilities;
import javax.swing.*;
import java.awt.*;

public class ElectionNewsInfluence extends JFrame {

    private static int electionCount = 1;
    private static double newsPartyScoreChartUpperBoundMax = 0;

    private NewsPaperPartyScoreChart newsPaperPartyScoreChart;
    private PartyCountChart partyCountChart;

    public ElectionNewsInfluence() {
        super("Election " + electionCount + " - News Influence");
    }

    public void compute() throws Exception {
        newsPaperPartyScoreChart = new NewsPaperPartyScoreChart();
        partyCountChart = new PartyCountChart();

        this.getContentPane().add(newsPaperPartyScoreChart.generateChartPanel(electionCount), BorderLayout.WEST);
        this.getContentPane().add(partyCountChart.generateChartPanel(electionCount), BorderLayout.EAST);
        this.pack();

        final double upperBound = newsPaperPartyScoreChart.getChartHeight();
        if (upperBound > newsPartyScoreChartUpperBoundMax)
            newsPartyScoreChartUpperBoundMax = upperBound;

        electionCount++;
    }

    public void showOnScreen() {
        newsPaperPartyScoreChart.setChartHeight(newsPartyScoreChartUpperBoundMax);
        partyCountChart.setChartHeight(Profiler.getPersons().size());
        RefineryUtilities.centerFrameOnScreen(this);
        this.setVisible(true);
    }
}
