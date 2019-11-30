package ar.edu.itba.model;

import lombok.Data;

@Data
public class Election {
    private String economicActions;
    private String economicTransitions;
    private String newsPapers;
    private String voters;

    public Election(String economicActions, String economicTransition, String newsPapers, String voters) {
        this.economicActions = economicActions;
        this.economicTransitions = economicTransition;
        this.voters = voters;
        this.newsPapers = newsPapers;
    }
}
