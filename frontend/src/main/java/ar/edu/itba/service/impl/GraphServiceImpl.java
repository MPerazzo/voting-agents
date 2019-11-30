package ar.edu.itba.service.impl;

import ar.edu.itba.model.Election;
import ar.edu.itba.model.Graphs;
import ar.edu.itba.service.GraphService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Service
public class GraphServiceImpl implements GraphService {

    @Override
    public Graphs getGraphs() throws Exception {
        final List<String> filesName = getFilesName();
        final Map<Integer, List<String>> graphsByElection = new HashMap<>();
        for (String fileName : filesName) {
            final String name = fileName.split("\\.")[0];
            final int electionNumber = Character.getNumericValue(name.charAt(name.length() - 1));
            if (!graphsByElection.containsKey(electionNumber))
                graphsByElection.put(electionNumber, new LinkedList<>());
            graphsByElection.get(electionNumber).add(name);
        }

        final List<Election> elections = new LinkedList<>();
        for (int i = 1 ; graphsByElection.containsKey(i) ; i++) {
            final List<String> graphFiles = order(graphsByElection.get(i));
            final Election e = new Election(toPng(graphFiles.get(0)), toPng(graphFiles.get(1)),
                    toPng(graphFiles.get(2)), toPng(graphFiles.get(3)));
            elections.add(e);
        }
        return new Graphs(elections);
    }

    private List<String> getFilesName() {
        List<String> filesName = new ArrayList<String>();

        File[] files = new File("src/main/resources/image").listFiles();

        for (File file : files) {
            if (file.isFile()) {
                filesName.add(file.getName());
            }
        }
        return filesName;
    }

    private List<String> order(final List<String> unOrdered) throws Exception {
        final List<String> ordered = new LinkedList<>();
        for (String s : unOrdered) {
            int index = getIndex(s);
            ordered.add(index, s);
        }
        return ordered;
    }

    private int getIndex(final String s) throws Exception {
        if (s.matches("economicActions[1-9]+"))
            return 0;
        else if (s.matches("economicTransitions[1-9]+"))
            return 1;
        else if (s.matches("newsPapers[1-9]+"))
            return 2;
        else if (s.matches("partyCount[1-9]+"))
            return 3;
        else
            throw new Exception("Invalid resource file");
    }

    private String toPng(final String name) {
        return name + ".png";
    }
}
