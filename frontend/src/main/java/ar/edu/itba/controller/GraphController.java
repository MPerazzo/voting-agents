package ar.edu.itba.controller;

import ar.edu.itba.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


public class GraphController {

    private static final String TEMPLATE_GRAPHS = "graphs";

    @Autowired
    GraphService graphService;

    @GetMapping("/graphs")
    public String getTests(Model model, RedirectAttributes flash) {
        model.addAttribute("graphs", graphService.getGraphs());
        return TEMPLATE_GRAPHS;
    }
}
