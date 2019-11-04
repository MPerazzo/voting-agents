package ar.edu.itba.model.config;


import ar.edu.itba.model.config.profile.MinistryPartyEconomic;

import java.util.List;


public class ConfigEconomicMinistry {
    private double prob;
    private String name;
    private List<MinistryPartyEconomic> economic;

    public String getName() { return name; }

    public double getProb() {
        return prob;
    }

    public List<MinistryPartyEconomic> getEconomic() { return economic; }
}
