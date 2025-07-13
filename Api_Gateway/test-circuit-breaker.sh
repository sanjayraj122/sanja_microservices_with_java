#!/bin/bash

# Circuit Breaker Test Script
# This script tests the circuit breaker functionality by simulating various load conditions

API_GATEWAY_URL="http://localhost:8086"
DELAY_BETWEEN_REQUESTS=0.1

echo "=== Circuit Breaker Testing Script ==="
echo "API Gateway URL: $API_GATEWAY_URL"
echo "========================================"

# Function to check circuit breaker health
check_circuit_breaker_health() {
    echo -e "\n📊 Checking Circuit Breaker Health..."
    curl -s "$API_GATEWAY_URL/actuator/circuitbreaker/health" | jq . 2>/dev/null || curl -s "$API_GATEWAY_URL/actuator/circuitbreaker/health"
}

# Function to get specific circuit breaker details
get_circuit_breaker_details() {
    local service_name=$1
    echo -e "\n🔍 Getting details for $service_name..."
    curl -s "$API_GATEWAY_URL/actuator/circuitbreaker/details/$service_name" | jq . 2>/dev/null || curl -s "$API_GATEWAY_URL/actuator/circuitbreaker/details/$service_name"
}

# Function to test a specific endpoint
test_endpoint() {
    local endpoint=$1
    local method=${2:-GET}
    local data=${3:-""}
    local description=$4
    
    echo -e "\n🧪 Testing: $description"
    echo "Endpoint: $method $endpoint"
    
    if [ "$method" = "POST" ] && [ -n "$data" ]; then
        response=$(curl -s -X POST "$API_GATEWAY_URL$endpoint" \
            -H "Content-Type: application/json" \
            -d "$data" \
            -w "\nHTTP Status: %{http_code}\nResponse Time: %{time_total}s\n")
    else
        response=$(curl -s -X "$method" "$API_GATEWAY_URL$endpoint" \
            -w "\nHTTP Status: %{http_code}\nResponse Time: %{time_total}s\n")
    fi
    
    echo "$response"
    echo "---"
}

# Function to simulate load on an endpoint
simulate_load() {
    local endpoint=$1
    local count=${2:-10}
    local description=$3
    
    echo -e "\n🚀 Simulating load: $description"
    echo "Endpoint: $endpoint"
    echo "Request count: $count"
    
    for i in $(seq 1 $count); do
        echo -n "Request $i/$count... "
        
        response=$(curl -s "$API_GATEWAY_URL$endpoint" \
            -w "HTTP:%{http_code} Time:%{time_total}s" \
            -o /dev/null)
        
        echo "$response"
        sleep $DELAY_BETWEEN_REQUESTS
    done
}

# Main testing sequence
main() {
    echo -e "\n🏁 Starting Circuit Breaker Tests..."
    
    # Initial health check
    check_circuit_breaker_health
    
    # Test 1: Normal operation
    echo -e "\n=== Test 1: Normal Operation ==="
    test_endpoint "/auth/test" "GET" "" "Auth Service - Normal Request"
    test_endpoint "/orders/test" "GET" "" "Restaurant Service - Normal Request"
    test_endpoint "/zomato/orders/test" "GET" "" "Zomato Service - Normal Request"
    
    # Test 2: Check circuit breaker states after normal operation
    echo -e "\n=== Test 2: Circuit Breaker States After Normal Operation ==="
    get_circuit_breaker_details "auth-service"
    get_circuit_breaker_details "restaurant-service"
    get_circuit_breaker_details "zomato-service"
    
    # Test 3: Simulate load to trigger circuit breaker
    echo -e "\n=== Test 3: Load Testing to Trigger Circuit Breaker ==="
    simulate_load "/auth/nonexistent" 15 "Auth Service - Heavy Load (should trigger circuit breaker)"
    
    # Check circuit breaker state after load
    echo -e "\n📊 Circuit Breaker State After Load Test:"
    get_circuit_breaker_details "auth-service"
    
    # Test 4: Test fallback responses
    echo -e "\n=== Test 4: Testing Fallback Responses ==="
    test_endpoint "/fallback/auth-service" "GET" "" "Auth Service Fallback"
    test_endpoint "/fallback/restaurant-service" "GET" "" "Restaurant Service Fallback"
    test_endpoint "/fallback/zomato-service" "GET" "" "Zomato Service Fallback"
    
    # Test 5: Monitor circuit breaker recovery
    echo -e "\n=== Test 5: Circuit Breaker Recovery Monitoring ==="
    echo "Waiting 5 seconds for potential recovery..."
    sleep 5
    
    check_circuit_breaker_health
    
    # Test 6: Test with authentication (if auth service is working)
    echo -e "\n=== Test 6: Authentication Test ==="
    test_endpoint "/auth/login" "POST" '{"username":"test","password":"test"}' "Auth Service - Login Request"
    
    # Final health check
    echo -e "\n=== Final Health Check ==="
    check_circuit_breaker_health
    
    echo -e "\n✅ Circuit Breaker Testing Complete!"
    echo "Check the API Gateway logs for detailed circuit breaker events."
}

# Check if required tools are available
check_requirements() {
    if ! command -v curl &> /dev/null; then
        echo "❌ Error: curl is required but not installed."
        exit 1
    fi
    
    if ! command -v jq &> /dev/null; then
        echo "⚠️  Warning: jq is not installed. JSON responses will not be formatted."
    fi
}

# Help function
show_help() {
    cat << EOF
Circuit Breaker Test Script

Usage: $0 [OPTIONS]

OPTIONS:
    -h, --help          Show this help message
    -u, --url URL       Set API Gateway URL (default: http://localhost:8086)
    -d, --delay DELAY   Set delay between requests in seconds (default: 0.1)
    -t, --test TEST     Run specific test (normal, load, fallback, recovery)

Examples:
    $0                          # Run all tests
    $0 -u http://localhost:8080 # Use different URL
    $0 -t load                  # Run only load test
    $0 -d 0.5                   # Set 0.5s delay between requests

EOF
}

# Parse command line arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        -h|--help)
            show_help
            exit 0
            ;;
        -u|--url)
            API_GATEWAY_URL="$2"
            shift 2
            ;;
        -d|--delay)
            DELAY_BETWEEN_REQUESTS="$2"
            shift 2
            ;;
        -t|--test)
            TEST_TYPE="$2"
            shift 2
            ;;
        *)
            echo "Unknown option: $1"
            show_help
            exit 1
            ;;
    esac
done

# Check requirements and run tests
check_requirements

if [ -n "$TEST_TYPE" ]; then
    case $TEST_TYPE in
        normal)
            echo "Running normal operation test..."
            test_endpoint "/auth/test" "GET" "" "Auth Service - Normal Request"
            ;;
        load)
            echo "Running load test..."
            simulate_load "/auth/nonexistent" 15 "Auth Service - Heavy Load"
            ;;
        fallback)
            echo "Running fallback test..."
            test_endpoint "/fallback/auth-service" "GET" "" "Auth Service Fallback"
            ;;
        recovery)
            echo "Running recovery test..."
            check_circuit_breaker_health
            ;;
        *)
            echo "Unknown test type: $TEST_TYPE"
            show_help
            exit 1
            ;;
    esac
else
    main
fi