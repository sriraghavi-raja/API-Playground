package com.api.playground.playground.controller;



import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/docs")
public class DocsController {

    @GetMapping
    public List<Map<String, String>> getDocs() {
        List<Map<String, String>> docs = new ArrayList<>();

        docs.add(Map.of(
            "title", "Echo API",
            "endpoint", "/api/echo?message=hello",
            "method", "GET",
            "description", "Returns the sent message with a timestamp."
        ));

        docs.add(Map.of(
            "title", "Calculator API",
            "endpoint", "/api/calculate?a=5&b=2&operation=add",
            "method", "GET",
            "description", "Performs a basic arithmetic operation."
        ));

        docs.add(Map.of(
            "title", "Fun Fact API",
            "endpoint", "/api/fun/fact",
            "method", "GET",
            "description", "Returns a random fun fact."
        ));

        return docs;
    }
}