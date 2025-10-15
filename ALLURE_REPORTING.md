# Allure Test Reporting

This project uses Allure Framework for generating beautiful and comprehensive test reports.

## Prerequisites

1. **Java 17+** - Required for running tests
2. **Allure Commandline** - For generating and serving reports

### Installing Allure Commandline

#### macOS (using Homebrew)
```bash
brew install allure
```

#### Windows (using Scoop)
```bash
scoop install allure
```

#### Linux (using npm)
```bash
npm install -g allure-commandline
```

#### Manual Installation
1. Download from [Allure Releases](https://github.com/allure-framework/allure2/releases)
2. Extract and add to PATH

## Running Tests with Allure

### 1. Run Tests
```bash
./gradlew test
```

This will generate Allure results in `build/allure-results/` directory.

### 2. Generate Report
```bash
./gradlew allureReport
```

This generates the HTML report in `build/reports/allure-report/` directory.

### 3. Serve Report (Interactive)
```bash
./gradlew allureServe
```

This generates the report and opens it in your default browser automatically.

### 4. Alternative: Manual Report Generation
```bash
# Generate report
allure generate build/allure-results --clean -o build/reports/allure-report

# Serve report
allure serve build/allure-results
```

## Allure Features Used

### Test Organization
- **Epics**: High-level feature groups (Integration Tests, Unit Tests)
- **Features**: Specific functionality areas (Review Controller, Review Service, Application Context)
- **Stories**: User stories or test scenarios (Review Generation, Input Validation, Health Check)

### Test Annotations
- `@DisplayName`: Human-readable test names
- `@Description`: Detailed test descriptions
- `@Severity`: Test importance levels (CRITICAL, NORMAL, MINOR, TRIVIAL)
- `@Story`: User story categorization

### Test Categories
- **Integration Tests**: End-to-end testing with Spring Boot context
- **Unit Tests**: Isolated component testing with mocks
- **Validation Tests**: Input validation and error handling
- **Health Check Tests**: Service availability verification

## Report Structure

The Allure report provides:

1. **Overview**: Test execution summary with statistics
2. **Categories**: Tests grouped by severity and status
3. **Suites**: Tests organized by test classes
4. **Graphs**: Visual representation of test results
5. **Timeline**: Test execution timeline
6. **Behaviors**: Tests organized by BDD structure (Epics, Features, Stories)
7. **Packages**: Tests organized by package structure

## Configuration

### Allure Properties (`allure.properties`)
- Results directory: `build/allure-results`
- Issue tracking links (customize for your repository)
- Test management system links

### Gradle Configuration (`build.gradle`)
- Allure plugin version: 2.11.2
- JUnit5 adapter version: 2.24.0
- AspectJ weaver enabled for step-by-step reporting

## Best Practices

1. **Use Descriptive Names**: Always use `@DisplayName` for human-readable test names
2. **Add Descriptions**: Use `@Description` to explain what the test validates
3. **Set Severity**: Use `@Severity` to indicate test importance
4. **Organize with Stories**: Use `@Story` to group related tests
5. **Use Epics and Features**: Organize tests hierarchically

## Troubleshooting

### Common Issues

1. **Allure command not found**
   - Ensure Allure commandline is installed and in PATH
   - Try using `./gradlew allureServe` instead

2. **No test results generated**
   - Ensure tests are running successfully: `./gradlew test`
   - Check that `build/allure-results/` directory exists and contains files

3. **Report not opening in browser**
   - Try manual generation: `allure serve build/allure-results`
   - Check if port 8080 is available

### Clean Build
If you encounter issues, try a clean build:
```bash
./gradlew clean test allureReport
```

## Integration with CI/CD

For CI/CD integration, you can:

1. **Generate reports in CI**:
   ```bash
   ./gradlew test allureReport
   ```

2. **Archive reports**:
   ```bash
   # Archive the report directory
   tar -czf allure-report.tar.gz build/reports/allure-report/
   ```

3. **Publish to Allure Server** (if using Allure Server):
   ```bash
   allure send results build/allure-results --endpoint http://allure-server:port
   ```

## Example Test Structure

```java
@Epic("Integration Tests")
@Feature("Review Controller")
class ReviewControllerTest {
    
    @Test
    @DisplayName("Should generate review and post to GitHub successfully")
    @Story("Review Generation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Tests the complete flow of generating a code review and posting it to GitHub PR")
    void testGenerateReviewWithPosting() {
        // Test implementation
    }
}
```

This structure provides clear organization and rich reporting capabilities for your test suite.
