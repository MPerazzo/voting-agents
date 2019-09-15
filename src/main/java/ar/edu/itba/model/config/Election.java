package ar.edu.itba.model.config;

import ar.edu.itba.model.config.profile.Party;

import java.util.List;

public class Election {
    private String initialRuler;
    private int period;
    private List<Party> partiesList;

    public String getInitialRuler() {
        return initialRuler;
    }

    public int getPeriod() {
        return period;
    }

    public List<Party> getPartiesList(){ return partiesList; }
}
