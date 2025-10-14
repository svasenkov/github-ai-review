package ai.review.integration;

import ai.review.dto.ReviewRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Factory class for creating test data objects
 */
public class TestDataFactory {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public static ReviewRequest createValidReviewRequest() {
        return new ReviewRequest(
            "test-owner/test-repo",
            123,
            true,
            "prompt-template.txt"
        );
    }
    
    public static ReviewRequest createReviewRequestWithoutPosting() {
        return new ReviewRequest(
            "test-owner/test-repo",
            123,
            false,
            "prompt-template.txt"
        );
    }
    
    public static ReviewRequest createReviewRequestWithCustomTemplate() {
        return new ReviewRequest(
            "test-owner/test-repo",
            123,
            true,
            "qa-automation-prompt-template.txt"
        );
    }
    
    public static String createSamplePullRequestDiff() {
        return """
            diff --git a/src/main/java/Example.java b/src/main/java/Example.java
            index 1234567..abcdefg 100644
            --- a/src/main/java/Example.java
            +++ b/src/main/java/Example.java
            @@ -1,5 +1,6 @@
             package com.example;
             
             public class Example {
            +    // Added a new comment
                 public void method() {
                     System.out.println("Hello World");
                 }
            """;
    }
    
    public static String createSamplePullRequestJson() {
        return """
            {
                "number": 123,
                "title": "Test Pull Request",
                "body": "This is a test pull request",
                "state": "open",
                "head": {
                    "ref": "feature-branch"
                },
                "base": {
                    "ref": "main"
                }
            }
            """;
    }
    
    public static String createSampleOllamaResponse() {
        return """
            {
                "response": "This is a comprehensive code review. The changes look good overall, but I have a few suggestions:\\n\\n1. Consider adding error handling\\n2. The new comment is helpful\\n3. Overall code quality is good"
            }
            """;
    }
    
    public static JsonNode createSamplePullRequestJsonNode() {
        try {
            return objectMapper.readTree(createSamplePullRequestJson());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create JSON node", e);
        }
    }
}
