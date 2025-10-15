#!/bin/bash

# Allure Test Reporting Script
# This script runs tests and generates Allure reports

echo "🧪 Running tests with Allure reporting..."
echo "========================================"

# Run tests
echo "1. Running tests..."
./gradlew clean test

if [ $? -eq 0 ]; then
    echo "✅ Tests completed successfully!"
    
    # Generate report
    echo "2. Generating Allure report..."
    ./gradlew allureReport
    
    if [ $? -eq 0 ]; then
        echo "✅ Allure report generated successfully!"
        echo ""
        echo "📊 Report location: build/reports/allure-report/allureReport/index.html"
        echo ""
        echo "🚀 To view the report interactively, run:"
        echo "   ./gradlew allureServe"
        echo ""
        echo "   Or open the report directly:"
        echo "   open build/reports/allure-report/allureReport/index.html"
    else
        echo "❌ Failed to generate Allure report"
        exit 1
    fi
else
    echo "❌ Tests failed!"
    exit 1
fi
