package ar.edu.itba.ui.election;

import org.jfree.ui.RefineryUtilities;
import javax.swing.*;
import java.awt.*;

public class ElectionNewsInfluence extends JFrame {

    private static int electionCount = 1;

    private static double scoreChartUpperBoundMax = 0;
    private static double partyCountChartUpperBoundMax = 0;

    private NewsPaperPartyScoreChart newsPaperPartyScoreChart;
    private NewsPaperPartyRealScoreChart newsPaperPartyRealScoreChart;
    private FriendScoreChart friendScoreChart;
    private PartyCountChart partyCountChart;
    private EconomicActionScoreChart economicActionScoreChart;

    public ElectionNewsInfluence() {
        super("Election " + electionCount + " - Details");
    }

    public void compute() throws Exception {
        newsPaperPartyScoreChart = new NewsPaperPartyScoreChart("Election " + electionCount + " - total score by newspaper and party");
        newsPaperPartyRealScoreChart = new NewsPaperPartyRealScoreChart("Election " + electionCount + " - real score by newspaper and party");
        friendScoreChart = new FriendScoreChart("Election " + electionCount + " - friendships influence");
        partyCountChart = new PartyCountChart("Election " + electionCount + " - party voters");
        economicActionScoreChart = new EconomicActionScoreChart("Election " + electionCount + " - economic actions " );

        this.setLayout(new FlowLayout());
        this.getContentPane().add(newsPaperPartyScoreChart.generateChartPanel());
        this.getContentPane().add(economicActionScoreChart.generateChartPanel());
        this.getContentPane().add(partyCountChart.generateChartPanel());
        this.getContentPane().add(newsPaperPartyRealScoreChart.generateChartPanel());
        this.getContentPane().add(friendScoreChart.generateChartPanel());
        this.pack();

        updateBounds();

        electionCount++;
    }

    private void updateBounds() {
        double upperBound = newsPaperPartyScoreChart.getChartHeight();
        if (upperBound > scoreChartUpperBoundMax)
            scoreChartUpperBoundMax = upperBound;

        upperBound = newsPaperPartyRealScoreChart.getChartHeight();
        if (upperBound > scoreChartUpperBoundMax)
            scoreChartUpperBoundMax = upperBound;

        upperBound = friendScoreChart.getChartHeight();
        if (upperBound > scoreChartUpperBoundMax)
            scoreChartUpperBoundMax = upperBound;

        upperBound = partyCountChart.getChartHeight();
        if (upperBound > partyCountChartUpperBoundMax)
            partyCountChartUpperBoundMax = upperBound;

        upperBound = economicActionScoreChart.getChartHeight();
        if (upperBound > scoreChartUpperBoundMax)
            scoreChartUpperBoundMax = upperBound;

    }

    public void showOnScreen() {
        newsPaperPartyScoreChart.setChartHeight(scoreChartUpperBoundMax);
        newsPaperPartyRealScoreChart.setChartHeight(scoreChartUpperBoundMax);
        //economicActionScoreChart.setChartHeight(scoreChartUpperBoundMax);
        friendScoreChart.setChartHeight(scoreChartUpperBoundMax);
        partyCountChart.setChartHeight(partyCountChartUpperBoundMax);
        RefineryUtilities.centerFrameOnScreen(this);
        this.setVisible(true);
    }
}
