package ai.review.integration;

import ai.review.controller.ReviewController;
import ai.review.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Basic integration test to verify Spring Boot application context loads correctly
 */
@SpringBootTest
@ActiveProfiles("test")
class BasicIntegrationTest {

    @Autowired
    private ReviewController reviewController;

    @Autowired
    private ReviewService reviewService;

    @Test
    void contextLoads() {
        assertThat(reviewController).isNotNull();
        assertThat(reviewService).isNotNull();
    }

    @Test
    void testHealthEndpoint() {
        // This test verifies that the application can start and the health endpoint is accessible
        assertThat(reviewController.health().getBody()).isEqualTo("Review service is running");
    }
}
