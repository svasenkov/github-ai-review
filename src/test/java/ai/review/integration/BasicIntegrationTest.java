package ai.review.integration;

import ai.review.controller.ReviewController;
import ai.review.service.ReviewService;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
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
@Epic("Integration Tests")
@Feature("Application Context")
class BasicIntegrationTest {

    @Autowired
    private ReviewController reviewController;

    @Autowired
    private ReviewService reviewService;

    @Test
    @DisplayName("Application context should load successfully")
    @Story("Context Loading")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies that Spring Boot application context loads correctly and all required beans are available")
    void contextLoads() {
        assertThat(reviewController).isNotNull();
        assertThat(reviewService).isNotNull();
    }

    @Test
    @DisplayName("Health endpoint should return correct response")
    @Story("Health Check")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that the application can start and the health endpoint is accessible")
    void testHealthEndpoint() {
        // This test verifies that the application can start and the health endpoint is accessible
        assertThat(reviewController.health().getBody()).isEqualTo("Review service is running");
    }
}
