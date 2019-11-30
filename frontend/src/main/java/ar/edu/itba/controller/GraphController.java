package ar.edu.itba.controller;

import ar.edu.itba.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class GraphController {

    private static final String TEMPLATE_GRAPHS = "graphs";

    @Autowired
    GraphService graphService;

    @GetMapping({"/", "/graphs"})
    public String getGraphs(Model model, RedirectAttributes flash) throws Exception {
        model.addAttribute("graphs", graphService.getGraphs());
        return TEMPLATE_GRAPHS;
    }
}
