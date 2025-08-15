package com.example.projet_gestion_absences.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class RouteController {

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @GetMapping("/routes")
    public List<String> listAllRoutes() {
        return requestMappingHandlerMapping
                .getHandlerMethods()
                .keySet()
                .stream()
                .map(mapping ->
                        mapping.getMethodsCondition().getMethods().isEmpty() ?
                                "ALL" : mapping.getMethodsCondition().getMethods().toString()
                                + " " + mapping.getPatternsCondition().getPatterns()
                )
                .collect(Collectors.toList());
    }
}