package com.example.ragdemo.service;

import com.example.ragdemo.model.Document;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VectorStoreService {

    private final List<Document> documents;
    private final Map<String, float[]> embeddingsMap;

    public VectorStoreService() {
        documents = new ArrayList<>();
        embeddingsMap = new HashMap<>();

        // Hardcoded documents
        documents.add(new Document("1", "Spring Boot makes it easy to create stand-alone, production-grade Spring applications."));
        documents.add(new Document("2", "Spring AI provides integration with OpenAI for embeddings and chat."));
        documents.add(new Document("3", "RAG stands for Retrieval-Augmented Generation."));

        // Generate mock embeddings (hash based)
        for (Document doc : documents) {
            float[] embedding = generateMockEmbedding(doc.getText());
            embeddingsMap.put(doc.getId(), embedding);
        }
    }

    // Search topK documents using cosine similarity
    public List<Document> search(String query, int topK) {
        float[] queryEmbedding = generateMockEmbedding(query);

        List<DocumentScore> scores = new ArrayList<>();
        for (Document doc : documents) {
            float[] docEmbedding = embeddingsMap.get(doc.getId());
            float score = cosineSimilarity(queryEmbedding, docEmbedding);
            scores.add(new DocumentScore(doc, score));
        }

        // Sort descending by score
        scores.sort((a, b) -> Float.compare(b.score, a.score));

        List<Document> results = new ArrayList<>();
        for (int i = 0; i < Math.min(topK, scores.size()); i++) {
            results.add(scores.get(i).doc);
        }
        return results;
    }

    // Naive mock embedding: convert string chars to floats
    private float[] generateMockEmbedding(String text) {
        float[] emb = new float[64];
        for (int i = 0; i < text.length() && i < emb.length; i++) {
            emb[i] = (float) text.charAt(i) / 128f;
        }
        return emb;
    }

    private float cosineSimilarity(float[] a, float[] b) {
        float dot = 0, normA = 0, normB = 0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }
        return dot / ((float)Math.sqrt(normA) * (float)Math.sqrt(normB) + 1e-8f);
    }

    private static class DocumentScore {
        Document doc;
        float score;
        DocumentScore(Document doc, float score) {
            this.doc = doc;
            this.score = score;
        }
    }
}
