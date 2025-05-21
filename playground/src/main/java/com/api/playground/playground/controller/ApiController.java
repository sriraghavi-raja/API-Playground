package com.api.playground.playground.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ApiController {
    private static final String WIKIPEDIA_API_URL = "https://en.wikipedia.org/w/api.php?action=query&format=json&prop=extracts&exintro&explaintext&titles=";
    private static final String STACKOVERFLOW_API_URL = "https://api.stackexchange.com/2.3/search/advanced?order=desc&sort=relevance&site=stackoverflow&q=";

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/learn")
    public Map<String, String> learn(@RequestParam String topic) {
        String url = WIKIPEDIA_API_URL + topic;
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        Map<String, Object> queryResult = (Map<String, Object>) response.getBody().get("query");
        Map<String, Object> pages = (Map<String, Object>) queryResult.get("pages");
        String pageId = pages.keySet().iterator().next();
        Map<String, Object> pageDetails = (Map<String, Object>) pages.get(pageId);
        String content = (String) pageDetails.get("extract");

        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("topic", topic);
        responseMap.put("content", content != null ? content : "No detailed information available for this topic.");

        return responseMap;
    }

    @GetMapping("/code-snippet")
    public Map<String, Object> getCodeSnippet(@RequestParam String query, @RequestParam String language) {
        String searchQuery = URLEncoder.encode(query + " in " + language + " site:stackoverflow.com", StandardCharsets.UTF_8);
        String url = STACKOVERFLOW_API_URL + searchQuery;

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        Map<String, Object> responseBody = response.getBody();

        Map<String, Object> result = new HashMap<>();
        result.put("query", query);
        result.put("language", language);
        result.put("data", responseBody);

        return result;
    }

    
    @GetMapping("/api/ai-tools")
    public ResponseEntity<Map<String, String>> getAiTool(@RequestParam String query) {
        Map<String, String> toolMap = new HashMap<>();
        switch (query.toLowerCase()) {
            case "frontend":
                toolMap.put("tool", "Boult AI");
                toolMap.put("link", "https://boult.ai/");
                break;
            case "study purpose":
                toolMap.put("tool", "ChatGPT");
                toolMap.put("link", "https://chat.openai.com/");
                break;
            case "presentation":
                toolMap.put("tool", "Beautiful.ai");
                toolMap.put("link", "https://www.beautiful.ai/");
                break;
            case "design":
                toolMap.put("tool", "Uizard");
                
                toolMap.put("link", "https://uizard.io/");
                break;
            case "coding":
                toolMap.put("tool", "GitHub Copilot");
                toolMap.put("link", "https://github.com/features/copilot");
                break;
            case "data science":
                toolMap.put("tool", "Kaggle AI");
                toolMap.put("link", "https://www.kaggle.com/");
                break;
            case "writing":
                toolMap.put("tool", "Quillbot");
                toolMap.put("link", "https://quillbot.com/");
                break;
            case "research":
                toolMap.put("tool", "Elicit");
                toolMap.put("link", "https://elicit.org/");
                break;
            case "summarizing":
                toolMap.put("tool", "TLDR This");
                toolMap.put("link", "https://tldrthis.com/");
                break;
            case "notes":
                toolMap.put("tool", "Notion AI");
                toolMap.put("link", "https://www.notion.so/product/ai");
                break;
            case "interview preparation":
                toolMap.put("tool", "Pramp");
                toolMap.put("link", "https://www.pramp.com/");
                break;
            case "voice typing":
                toolMap.put("tool", "Speechnotes");
                toolMap.put("link", "https://speechnotes.co/");
                break;
            case "video editing":
                toolMap.put("tool", "Pictory AI");
                toolMap.put("link", "https://pictory.ai/");
                break;
            case "resume building":
                toolMap.put("tool", "Rezi AI");
                toolMap.put("link", "https://www.rezi.ai/");
                break;
            case "chatbot creation":
                toolMap.put("tool", "Tidio AI");
                toolMap.put("link", "https://www.tidio.com/");
                break;
            case "pdf summarizer":
                toolMap.put("tool", "Humata AI");
                toolMap.put("link", "https://www.humata.ai/");
                break;
            default:
                toolMap.put("tool", "No matching tool found");
                toolMap.put("link", "#");
        }

        return ResponseEntity.ok(toolMap);
    }

    @GetMapping("/echo")
    public Map<String, Object> echo(@RequestParam String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("echo", message);
        response.put("timestamp", LocalDateTime.now().toString());
        return response;
    }

    @GetMapping("/calculate")
    public Map<String, Object> calculate(@RequestParam double a,
                                         @RequestParam double b,
                                         @RequestParam String operation) {
        Map<String, Object> response = new HashMap<>();
        double result;
        switch (operation) {
            case "add": result = a + b; break;
            case "sub": result = a - b; break;
            case "mul": result = a * b; break;
            case "div": result = b != 0 ? a / b : Double.NaN; break;
            default: result = Double.NaN; break;
        }
        response.put("result", result);
        response.put("operation", operation);
        return response;
    }

    @GetMapping("/fun/fact")
    public Map<String, String> funFact() {
        List<String> facts = Arrays.asList(
            "Honey never spoils.",
            "Bananas are berries, but strawberries aren't.",
            "A group of flamingos is called a 'flamboyance'."
        );
        Map<String, String> response = new HashMap<>();
        response.put("fact", facts.get(new Random().nextInt(facts.size())));
        return response;
    }

    @GetMapping("/help")
    public Map<String, String> help() {
        Map<String, String> helpInfo = new LinkedHashMap<>();
        helpInfo.put("/learn?topic=AI", "Get an explanation of a CS topic.");
        helpInfo.put("/code-snippet?query=for+loop&language=python", "Fetch code snippets from StackOverflow.");
        helpInfo.put("/echo?message=hello", "Echo back your message.");
        helpInfo.put("/calculate?a=5&b=3&operation=add", "Perform calculations.");
        helpInfo.put("/fun/fact", "Get a random fun fact.");
        return helpInfo;
    }
}
