package ar.edu.itba.ui.election;

import org.apache.commons.io.FileUtils;
import ar.edu.itba.model.Person;
import ar.edu.itba.model.enums.SocialClass;
import ar.edu.itba.model.handlers.Profiler;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.category.BarRenderer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ElectionUI extends JFrame {

    private static Map<SocialClass, Long> previousCount = new HashMap<>();

    private static int electionCount = 1;
    private int electionNumber;

    private static double scoreChartUpperBoundMax = 0;
    private static double partyCountChartUpperBoundMax = 0;
    private static double economicTransitionLowerBoundMin = 0;
    private static double economicTransitionUpperBoundMax = 0;

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

        electionNumber = electionCount;

        updateGraphs();

        electionCount++;
    }

    private void updateBounds() {
        double height = newsPaperPartyScoreChart.getChartHeight();
        if (height > scoreChartUpperBoundMax)
            scoreChartUpperBoundMax = height;

        height = partyCountChart.getChartHeight();
        if (height > partyCountChartUpperBoundMax)
            partyCountChartUpperBoundMax = height;

        height = economicClassChart.chart.getCategoryPlot().getRangeAxis().getUpperBound();
        if (height > economicTransitionUpperBoundMax)
            economicTransitionUpperBoundMax = height;

        height = economicClassChart.chart.getCategoryPlot().getRangeAxis().getLowerBound();
        if (height < economicTransitionLowerBoundMin)
            economicTransitionLowerBoundMin = height;

        height = getElectionChartHeight();
        if (height > scoreChartUpperBoundMax)
            scoreChartUpperBoundMax = height;
    }

    public void updateGraphs() throws IOException {
        newsPaperPartyScoreChart.setChartHeight(scoreChartUpperBoundMax);
        partyCountChart.setChartHeight(partyCountChartUpperBoundMax);
        setElectionChartBounds();
        ((BarRenderer) economicActionScoreChart.chart.getCategoryPlot().getRenderer()).setMaximumBarWidth(.25);
        economicClassChart.chart.getCategoryPlot().getRangeAxis().setUpperBound(economicTransitionUpperBoundMax);
        economicClassChart.chart.getCategoryPlot().getRangeAxis().setLowerBound(economicTransitionLowerBoundMin);

        generateOutputImages();
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

    private void generateOutputImages() throws IOException {
        generateOutputImage(economicActionScoreChart.chart, "economicActions");
        generateOutputImage(economicClassChart.chart, "economicTransitions");
        generateOutputImage(partyCountChart.chart, "partyCount");
        generateOutputImage(newsPaperPartyScoreChart.chart, "newsPapers");
    }

    private void generateOutputImage(final JFreeChart chart, final String name) throws IOException {
        File outputFile = new File("resources/" + name + electionNumber + ".png");
        if (!ImageIO.write(chart.createBufferedImage(600, 600), "png", outputFile))
            throw new IOException("Couldnt write chart to image");
    }

    public static void setPreviousCount(final Map<SocialClass, Long> prevCount) {
        previousCount = prevCount;
    }

    public static Map<SocialClass, Long> getPreviousCount() {
        return previousCount;
    }


    public static Map<SocialClass, Long> calculateEconomicTransition(final List<Person> persons) {
        final Map<SocialClass, Long> count = persons.stream().collect(Collectors.groupingBy(Person::getSocialClass, Collectors.counting()));

        for (SocialClass s : SocialClass.values()) {
            if (!count.containsKey(s) || count.get(s) == null)
                count.put(s, 0L);
        }
        return count;
    }

    public static void init() throws IOException {
        clearAllImages();
        setPreviousCount(calculateEconomicTransition(Profiler.getInstance().getPersons()));
    }

    private static void clearAllImages() throws IOException {
        FileUtils.cleanDirectory(new File("resources"));
    }
}
