package ar.edu.itba.ui.election;

import org.jfree.ui.RefineryUtilities;
import javax.swing.*;
import java.awt.*;

public class ElectionNewsInfluence extends JFrame {

    private static int electionCount = 1;

    public static void compute() throws Exception {
        final ElectionNewsInfluence frame = new ElectionNewsInfluence("Election " + electionCount + " - News Influence");
        frame.getContentPane().add(new NewsPaperSubjectCount().generateChartPanel(electionCount), BorderLayout.EAST);
        frame.getContentPane().add(new NewsPaperPartyScoreChart().generateChartPanel(electionCount), BorderLayout.WEST);
        frame.pack();
        RefineryUtilities.centerFrameOnScreen(frame);
        frame.setVisible(true);
        electionCount++;
    }

    public ElectionNewsInfluence(String title) {
        super(title);
    }
}
