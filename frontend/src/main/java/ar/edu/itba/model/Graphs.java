package ar.edu.itba.model;

import lombok.Data;

import java.util.List;

@Data
public class Graphs {
    List<Election> elections;

    public Graphs(List<Election> elections) {
        this.elections = elections;
    }
}
