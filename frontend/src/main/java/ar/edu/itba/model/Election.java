package ar.edu.itba.model;

import lombok.Data;

@Data
public class Election {
    private String economicActions;
    private String economicTransition;
    private String voters;
    private String newsPapers;

    public Election(String economicActions, String economicTransition, String voters, String newsPapers) {
        this.economicActions = economicActions;
        this.economicTransition = economicTransition;
        this.voters = voters;
        this.newsPapers = newsPapers;
    }
}
