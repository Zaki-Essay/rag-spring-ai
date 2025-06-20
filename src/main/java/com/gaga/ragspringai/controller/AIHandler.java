package com.gaga.ragspringai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class AIHandler {

    private final ChatClient chatClient;

    public AIHandler(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/{message}")
    public String chat(@PathVariable String message) {
        return chatClient.prompt().user(message).call().content();
    }
}
