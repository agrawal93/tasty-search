package com.agrawal.tasty.search.controller;

import com.agrawal.tasty.search.service.QueryService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Hardik Agrawal [ hardik93@ymail.com ]
 */
@Controller
public class QuerySearch {

    @Autowired
    private QueryService service;

    @RequestMapping(
            method = { RequestMethod.GET, RequestMethod.POST },
            value = "/search",
            produces = {"application/json"}
    )
    public @ResponseBody
    List<String> search(Map<String, Object> model, @RequestParam("query") String query) {
        return search(model, query, 20);
    }

    @RequestMapping(
            method = { RequestMethod.GET, RequestMethod.POST },
            value = "/search/{K}",
            produces = {"application/json"}
    )
    public @ResponseBody
    List<String> search(Map<String, Object> model, @RequestParam("query") String query, @PathVariable int K) {
        return service.search(query.split("\\W+"));
    }

}
