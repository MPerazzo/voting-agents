package ar.edu.itba.ui.election;

import ar.edu.itba.model.News;
import ar.edu.itba.model.config.Configuration;
import ar.edu.itba.model.handlers.Media;
import ar.edu.itba.model.handlers.NewsPaper;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NewsPaperSubjectCount extends BaseStackedChart {

    public ChartPanel generateChartPanel(final int electionCount) throws Exception {
        final CategoryDataset dataset = createDataset();
        final JFreeChart chart = createChart(dataset, electionCount);
        chart.getCategoryPlot().getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        return generateChartPanel(chart, true);
    }

    protected CategoryDataset createDataset() throws Exception {
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

    private Map<String, Long> normalize(final Map<String, Long> subjectsCount) throws Exception {
        final Map<String, Long> normalizedCount = new HashMap<>();

        for (final String subject : Configuration.getInstance().getSubjects())
            normalizedCount.put(subject, 0L);

        for (final Map.Entry<String, Long> e : subjectsCount.entrySet())
            normalizedCount.put(e.getKey(), e.getValue());

        return normalizedCount;
    }

    protected JFreeChart createChart(final CategoryDataset dataset, final int electionCount) throws Exception {
        return super.createChart(dataset, "Election " + electionCount + " - Subject count by newspaper",
                "Subject", "Count");
    }
}
