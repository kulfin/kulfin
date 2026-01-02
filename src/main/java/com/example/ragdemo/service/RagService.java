package com.example.ragdemo.service;

import com.example.ragdemo.model.AskRequest;
import com.example.ragdemo.model.Document;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * RagService: Minimal RAG-style question answering without relying on unavailable Spring AI APIs.
 */
@Service
public class RagService {

    private final VectorStoreService vectorStoreService;

    public RagService(VectorStoreService vectorStoreService) {
        this.vectorStoreService = vectorStoreService;
    }

    /**
     * Answer the question using top-k documents from in-memory vector store.
     * @param request AskRequest containing the user question.
     * @return Answer string generated from the context.
     */
    public String answerQuestion(AskRequest request) {
        String question = request.getQuestion();

        // 1️⃣ Retrieve top 2 documents from vector store
        List<Document> docs = vectorStoreService.search(question, 2);

        if (docs.isEmpty()) {
            return "No relevant documents found to answer your question.";
        }

        // 2️⃣ Build context prompt
        StringBuilder context = new StringBuilder();
        for (Document doc : docs) {
            context.append("- ").append(doc.getText()).append("\n");
        }

        // 3️⃣ Construct final answer
        String answer = generateAnswerFromContext(question, context.toString());

        System.out.println("=== Prompt Context ===");
        System.out.println(context);

        System.out.println("=== Answer Generated ===");
        System.out.println(answer);

        return answer;
    }

    /**
     * Simple string-based answer generator (mock AI) that combines question and context.
     */
    private String generateAnswerFromContext(String question, String context) {
        // In real scenario, call OpenAI or Spring AI chat client
        // Here we simulate a minimal RAG-style answer
        StringBuilder sb = new StringBuilder();
        sb.append("Based on the retrieved documents:\n");
        sb.append(context);
        sb.append("\nAnswer to your question [").append(question).append("]:\n");

        // Pick first sentence from context as a mock answer
        String[] lines = context.split("\\n");
        if (lines.length > 0) {
            sb.append(lines[0]);
        } else {
            sb.append("Sorry, no answer could be generated.");
        }

        return sb.toString();
    }
}
