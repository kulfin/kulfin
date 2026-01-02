package com.example.ragdemo.controller;

import com.example.ragdemo.model.AskRequest;
import com.example.ragdemo.service.RagService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")  // base path
public class RagController {

    private final RagService ragService;

    public RagController(RagService ragService) {
        this.ragService = ragService;
    }

    @PostMapping("/ask")  // full path will be /api/ask
    public String ask(@RequestBody AskRequest request) {
        return ragService.answerQuestion(request);
    }
}
