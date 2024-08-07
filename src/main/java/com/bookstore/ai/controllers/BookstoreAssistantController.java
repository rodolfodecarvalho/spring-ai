package com.bookstore.ai.controllers;

import lombok.AllArgsConstructor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;


@RestController
@RequestMapping("/bookstore")
@AllArgsConstructor
public class BookstoreAssistantController {

    public static final String QUAIS_SÃO_OS_LIVROS_BEST_SELLERS_DOS_ULTIMOS_ANOS = "Quais são os livros best sellers dos ultimos anos?";
    private final OpenAiChatModel chatModel;

    @GetMapping("/generate")
    public Map<String,String> generate(@RequestParam(value = "message", defaultValue = QUAIS_SÃO_OS_LIVROS_BEST_SELLERS_DOS_ULTIMOS_ANOS) String message) {
        return Map.of("generation", chatModel.call(message));
    }

    @GetMapping("/informations")
    public String bookstoreChat(@RequestParam(value = "message",
            defaultValue = QUAIS_SÃO_OS_LIVROS_BEST_SELLERS_DOS_ULTIMOS_ANOS) String message) {
        return chatModel.call(message);
    }

    @GetMapping("/informations2")
    public ChatResponse bookstoreChatEx2(@RequestParam(value = "message",
            defaultValue = QUAIS_SÃO_OS_LIVROS_BEST_SELLERS_DOS_ULTIMOS_ANOS) String message) {
        return chatModel.call(new Prompt(message));
    }

    @GetMapping("/reviews")
    public String bookstoreReview(@RequestParam(value = "book", defaultValue = "Dom Quixote") String book) {
        PromptTemplate promptTemplate = new PromptTemplate("""
                  Por favor, me forneça
                  um breve resumo do livro {book}
                  e também a biografia de seu autor.
                """);
        promptTemplate.add("book", book);
        return this.chatModel.call(promptTemplate.create()).getResult().getOutput().getContent();
    }

    @GetMapping("/stream/informations")
    public Flux<String> bookstoreChatStream(@RequestParam(value = "message",
            defaultValue = QUAIS_SÃO_OS_LIVROS_BEST_SELLERS_DOS_ULTIMOS_ANOS) String message) {
        return chatModel.stream(message);
    }

    @GetMapping("/stream/informations2")
    public Flux<ChatResponse> bookstoreChatStreamEx2(@RequestParam(value = "message",
            defaultValue = QUAIS_SÃO_OS_LIVROS_BEST_SELLERS_DOS_ULTIMOS_ANOS) String message) {
        return chatModel.stream(new Prompt(message));
    }
}