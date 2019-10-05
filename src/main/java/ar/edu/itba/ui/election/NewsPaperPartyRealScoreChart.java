package ar.edu.itba.ui.election;

import ar.edu.itba.model.News;
import org.jfree.data.category.CategoryDataset;

//cambio de score por partido segun el medio
public class NewsPaperPartyRealScoreChart extends NewsPaperPartyBaseScoreChart {

    public NewsPaperPartyRealScoreChart(String title) {
        super(title);
    }

    protected CategoryDataset createDataset() throws Exception {
        return createDataset(News::getRealImpact);
    }
}
