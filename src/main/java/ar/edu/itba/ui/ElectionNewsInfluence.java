package ar.edu.itba.ui;

import ar.edu.itba.model.News;
import ar.edu.itba.model.config.Configuration;
import ar.edu.itba.model.handlers.Media;
import ar.edu.itba.model.handlers.NewsPaper;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.ui.RefineryUtilities;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ElectionNewsInfluence extends JFrame {

    private static final List<Color> colors = generateColors();
    private static int electionCount = 1;

    public static void compute() throws Exception {
        final ElectionNewsInfluence demo = new ElectionNewsInfluence("Election " + electionCount + " - News Influence");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }

    public ElectionNewsInfluence(String title) throws Exception {
        super(title);

        final CategoryDataset dataset = createDataset();

        //Default theme (bar colors, etc)
        ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
        BarRenderer.setDefaultBarPainter(new StandardBarPainter());

        final JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 350));
        setContentPane(chartPanel);
    }

    private CategoryDataset createDataset() throws Exception {
        final int subjects = Configuration.getInstance().getSubjects().size();
        final List<NewsPaper> newsPapers = Media.getSources();
        final double[][] data = new double[newsPapers.size()][subjects];

        int i = 0;
        for (final NewsPaper newsPaper : newsPapers) {
            final Map<String, Long> subjectsCount = newsPaper.getNews().stream().collect(Collectors.groupingBy(News::getSubject, Collectors.counting()));
            final Map<String, Long> normalizedCount = normalize(subjectsCount);
            data[i++] = normalizedCount.values().stream().mapToDouble(v -> v.intValue()).toArray();
        }

        String[] newsPapersId = newsPapers.stream().map(newsPaper -> newsPaper.getId()).toArray(String[]::new);
        String[] subjectsId = Configuration.getInstance().getSubjects().stream().toArray(String[]::new);
        return DatasetUtilities.createCategoryDataset(newsPapersId, subjectsId, data);
    }

    private JFreeChart createChart(final CategoryDataset dataset) throws Exception {

        final JFreeChart chart = ChartFactory.createStackedBarChart(
                "Election " + electionCount++ + " - News Influence", "", "Score",
                dataset, PlotOrientation.VERTICAL, true, true, false);

        chart.setBackgroundPaint(new Color(249, 231, 236));

        CategoryPlot plot = chart.getCategoryPlot();

        for (int i = 0 ; i < Configuration.getInstance().getPoliticalParties().size() ; i++)
            plot.getRenderer().setSeriesPaint(i, colors.get(i));

        return chart;
    }

    private Map<String, Long> normalize(final Map<String, Long> subjectsCount) throws Exception {
        final Map<String, Long> normalizedCount = new HashMap<>();

        for (final String subject : Configuration.getInstance().getSubjects())
            normalizedCount.put(subject, 0L);

        for (final Map.Entry<String, Long> e : subjectsCount.entrySet())
            normalizedCount.put(e.getKey(), e.getValue());

        return normalizedCount;
    }

    private static List<Color> generateColors() {
        final List<Color> colors = new LinkedList<>();
        colors.add(new Color(0, 0, 128));
        colors.add(new Color(128, 0, 0));
        colors.add(new Color(200, 160, 0));
        return colors;
    }
}
