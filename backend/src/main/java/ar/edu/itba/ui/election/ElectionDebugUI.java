package ar.edu.itba.ui.election;

import org.jfree.ui.RefineryUtilities;

import javax.swing.*;
import java.awt.*;

public class ElectionDebugUI extends JFrame {

    private static int electionCount = 1;

    private FriendScoreChart friendScoreChart;

    public ElectionDebugUI() {
        super("Election " + electionCount + " DEBUG - Details");
    }

    public void compute() throws Exception {
        friendScoreChart = new FriendScoreChart("Election " + electionCount + " DEBUG - friendships influence");

        this.setLayout(new FlowLayout());
        this.getContentPane().add(friendScoreChart.generateChartPanel());
        this.pack();

        electionCount++;
    }

    public void showOnScreen() {
        RefineryUtilities.centerFrameOnScreen(this);
        this.setVisible(true);
    }
}