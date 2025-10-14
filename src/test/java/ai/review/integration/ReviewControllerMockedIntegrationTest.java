package ai.review.integration;

import ai.review.dto.ReviewRequest;
import ai.review.dto.ReviewResponse;
import ai.review.github.GitHubClient;
import ai.review.ollama.OllamaClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Integration tests for the ReviewController using Mockito for external service mocking
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebMvc
@Epic("Integration Tests")
@Feature("Review Controller")
class ReviewControllerMockedIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private GitHubClient gitHubClient;

    @MockBean
    private OllamaClient ollamaClient;

    @Autowired
    private ObjectMapper objectMapper;

    private String baseUrl;

    @Test
    @DisplayName("Should generate review and post to GitHub successfully")
    @Story("Review Generation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Tests the complete flow of generating a code review and posting it to GitHub PR")
    void testGenerateReviewWithPosting() throws Exception {
        // Given
        baseUrl = "http://localhost:" + port + "/api/review";
        
        String sampleDiff = """
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
        
        String sampleReview = "This is a comprehensive code review. The changes look good overall, but I have a few suggestions:\n\n1. Consider adding error handling\n2. The new comment is helpful\n3. Overall code quality is good";
        
        when(gitHubClient.getPullRequestDiff(anyString(), anyInt())).thenReturn(sampleDiff);
        when(ollamaClient.generate(anyString())).thenReturn(sampleReview);
        
        ReviewRequest request = new ReviewRequest("test-owner/test-repo", 123, true, "prompt-template.txt");

        // When
        ResponseEntity<ReviewResponse> response = restTemplate.postForEntity(
            baseUrl,
            createHttpEntity(request),
            ReviewResponse.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getReview()).isNotEmpty();
        assertThat(response.getBody().isPostedToGitHub()).isTrue();
        assertThat(response.getBody().getMessage()).contains("posted to GitHub PR #123");
    }

    @Test
    @DisplayName("Should generate review without posting to GitHub")
    @Story("Review Generation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Tests generating a code review without posting it to GitHub")
    void testGenerateReviewWithoutPosting() throws Exception {
        // Given
        baseUrl = "http://localhost:" + port + "/api/review";
        
        String sampleDiff = "diff --git a/test.java b/test.java\n+public class Test {}";
        String sampleReview = "This is a test review";
        
        when(gitHubClient.getPullRequestDiff(anyString(), anyInt())).thenReturn(sampleDiff);
        when(ollamaClient.generate(anyString())).thenReturn(sampleReview);
        
        ReviewRequest request = new ReviewRequest("test-owner/test-repo", 123, false, "prompt-template.txt");

        // When
        ResponseEntity<ReviewResponse> response = restTemplate.postForEntity(
            baseUrl,
            createHttpEntity(request),
            ReviewResponse.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getReview()).isNotEmpty();
        assertThat(response.getBody().isPostedToGitHub()).isFalse();
        assertThat(response.getBody().getMessage()).isEqualTo("Review generated successfully");
    }

    @Test
    @DisplayName("Should return error for invalid repository format")
    @Story("Input Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Tests validation of repository format in review request")
    void testGenerateReviewWithInvalidRepository() throws Exception {
        // Given
        baseUrl = "http://localhost:" + port + "/api/review";
        ReviewRequest request = new ReviewRequest("invalid-repo", 123, false, "prompt-template.txt");

        // When
        ResponseEntity<String> response = restTemplate.postForEntity(
            baseUrl,
            createHttpEntity(request),
            String.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Invalid request parameters");
    }

    @Test
    @DisplayName("Should return error for invalid PR number")
    @Story("Input Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Tests validation of PR number in review request")
    void testGenerateReviewWithInvalidPrNumber() throws Exception {
        // Given
        baseUrl = "http://localhost:" + port + "/api/review";
        ReviewRequest request = new ReviewRequest("test-owner/test-repo", -1, false, "prompt-template.txt");

        // When
        ResponseEntity<String> response = restTemplate.postForEntity(
            baseUrl,
            createHttpEntity(request),
            String.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Invalid request parameters");
    }

    @Test
    @DisplayName("Health endpoint should return correct response")
    @Story("Health Check")
    @Severity(SeverityLevel.MINOR)
    @Description("Tests the health endpoint accessibility")
    void testHealthEndpoint() throws Exception {
        // Given
        baseUrl = "http://localhost:" + port + "/api/review";

        // When
        ResponseEntity<String> response = restTemplate.getForEntity(
            baseUrl + "/health",
            String.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Review service is running");
    }

    @Test
    @DisplayName("Should return error for empty request body")
    @Story("Input Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Tests handling of empty request body")
    void testGenerateReviewWithEmptyRequestBody() throws Exception {
        // Given
        baseUrl = "http://localhost:" + port + "/api/review";

        // When
        ResponseEntity<String> response = restTemplate.postForEntity(
            baseUrl,
            createHttpEntity(""),
            String.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Should return error for missing required fields")
    @Story("Input Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Tests validation of required fields in review request")
    void testGenerateReviewWithMissingFields() throws Exception {
        // Given
        baseUrl = "http://localhost:" + port + "/api/review";
        String requestJson = "{}";

        // When
        ResponseEntity<String> response = restTemplate.postForEntity(
            baseUrl,
            createHttpEntity(requestJson),
            String.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Invalid request parameters");
    }

    private HttpEntity<String> createHttpEntity(Object request) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        String requestBody;
        if (request instanceof String) {
            requestBody = (String) request;
        } else {
            requestBody = objectMapper.writeValueAsString(request);
        }
        
        return new HttpEntity<>(requestBody, headers);
    }
}
