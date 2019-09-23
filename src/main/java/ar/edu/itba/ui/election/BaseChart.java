package ar.edu.itba.ui.election;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;

public abstract class BaseChart {
    public abstract ChartPanel generateChartPanel(final int electionCount) throws Exception;
    protected abstract CategoryDataset createDataset() throws Exception;
    protected abstract JFreeChart createChart(final CategoryDataset dataset, final int electionCount) throws Exception;
}
