package ar.edu.itba.ui.election;

import ar.edu.itba.model.News;
import org.jfree.data.category.CategoryDataset;

public class NewsPaperPartyScoreChart extends NewsPaperPartyBaseScoreChart {

    public NewsPaperPartyScoreChart(String title) {
        super(title);
    }

    protected CategoryDataset createDataset() throws Exception {
        return createDataset(News::getTotalImpact);
    }
}
