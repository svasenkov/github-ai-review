# Integration Tests

This document describes the integration tests for the GitHub AI Review application.

## Overview

The integration tests use Spring Boot Test with Mockito to mock external services, providing a comprehensive testing environment that verifies the application's behavior without depending on external APIs.

## Test Structure

### Test Configuration

- **Spring Boot Test**: Provides the testing framework and full application context
- **Mockito**: Used to mock GitHub and Ollama clients
- **Test Profiles**: Uses `application-test.properties` for test-specific configuration
- **TestRestTemplate**: Used for making HTTP requests to test REST endpoints

### Test Files

1. **BasicIntegrationTest**: Verifies that the Spring application context loads correctly
2. **ReviewControllerMockedIntegrationTest**: Tests the REST API endpoints with mocked external services

## Running the Tests

### Prerequisites

- Java 17 or higher
- Gradle 8.0 or higher

### Running All Integration Tests

```bash
./gradlew test --tests "*IntegrationTest"
```

### Running Specific Integration Tests

```bash
# Run only the basic integration test
./gradlew test --tests "ai.review.integration.BasicIntegrationTest"

# Run only the controller integration test
./gradlew test --tests "ai.review.integration.ReviewControllerMockedIntegrationTest"
```

### Running with Verbose Output

```bash
./gradlew test --tests "*IntegrationTest" --info
```

## Test Configuration

### Application Properties

The test configuration uses `application-test.properties` which overrides the main application properties:

```properties
# GitHub API configuration (mocked with Mockito)
app.github.base-url=http://localhost:8080
app.github.token=test-token

# Ollama API configuration (mocked with Mockito)
app.ollama.api-url=http://localhost:8080/api/generate
app.ollama.api-token=test-ollama-token
```

### Mock Configuration

External services are mocked using Mockito:

- **GitHubClient**: Mocked to return test data for pull request diffs and comment posting
- **OllamaClient**: Mocked to return test AI-generated review content

## Test Coverage

### ReviewControllerMockedIntegrationTest

This test class covers:

1. **Health Endpoint**: Tests the `/api/review/health` endpoint
2. **Review Generation with Posting**: Tests review generation and posting to GitHub
3. **Review Generation without Posting**: Tests review generation without posting
4. **Validation Errors**: Tests various validation scenarios:
   - Invalid repository format
   - Invalid PR number
   - Missing required fields
   - Empty request body

### Test Scenarios

Each test verifies:
- Correct HTTP status codes
- Proper response body content
- Mock service interactions
- Error handling and validation

## Test Data

### TestDataFactory

The `TestDataFactory` class provides utility methods for creating test data:

- `createReviewRequest(boolean postToGitHub)`: Creates a `ReviewRequest` with test data
- `createReviewResponse(String review, boolean postedToGitHub)`: Creates a `ReviewResponse` with test data

### Mock Responses

The tests use predefined mock responses:

- **GitHub API**: Returns "diff content" for pull request diffs
- **Ollama API**: Returns "AI review content" for review generation

## Troubleshooting

### Common Issues

1. **Port conflicts**: The tests use random ports for the embedded Tomcat server
2. **Mock setup**: Ensure mocks are properly configured in the `@BeforeEach` method
3. **Test isolation**: Each test should be independent and not rely on other tests

### Debug Mode

To run tests with more verbose output:

```bash
./gradlew test --tests "*IntegrationTest" --info
```

### Test Logs

Test logs are available in the `build/reports/tests/test/` directory after running the tests.

## Best Practices

1. **Isolation**: Each test should be independent and not rely on other tests
2. **Mocking**: Use Mockito to mock external services to avoid dependencies on real APIs
3. **Test Data**: Use the `TestDataFactory` for consistent test data creation
4. **Assertions**: Use AssertJ for readable and powerful assertions
5. **Verification**: Verify that mocked services are called with expected parameters

## Continuous Integration

These integration tests are designed to run in CI/CD pipelines. They don't require Docker or external services, making them suitable for any CI environment with Java and Gradle support.

## Test Results

All integration tests are currently passing:

- ✅ BasicIntegrationTest: Verifies Spring context loads correctly
- ✅ ReviewControllerMockedIntegrationTest: Tests all REST API endpoints and validation scenarios

The test suite provides comprehensive coverage of the application's functionality while maintaining fast execution times and reliable results.