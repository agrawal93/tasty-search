package com.agrawal.tasty.search.controller;

import com.agrawal.tasty.search.service.QueryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author I322819
 */
@Controller
public class QuerySearch {
    
    @Autowired
    private QueryService service;
    
    @GetMapping(
            value = "/search",
            produces = { "application/json" }
    )
    public @ResponseBody List<String> search(@RequestParam("query") String query) {
        return service.search(query.split(","));
    }
    
    @GetMapping("/search/{K}")
    public @ResponseBody List<String> search(@RequestParam("query") String query, @PathVariable int K) {
        return service.search(query.split(","), K);
    }
    
}
