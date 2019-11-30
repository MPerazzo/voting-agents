package ar.edu.itba.service.impl;

import ar.edu.itba.model.Graphs;
import ar.edu.itba.service.GraphService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class GraphServiceImpl implements GraphService {

    @Override
    public Graphs getGraphs() {
        List<String> filesName = getFilesName();

        return null;
    }

    private List<String> getFilesName() {
        List<String> filesName = new ArrayList<String>();

        File[] files = new File("resources").listFiles();

        for (File file : files) {
            if (file.isFile()) {
                filesName.add(file.getName());
            }
        }
        return filesName;
    }
}
