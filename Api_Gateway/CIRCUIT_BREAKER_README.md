# Circuit Breaker Implementation for API Gateway

## Overview

This implementation provides a robust circuit breaker pattern for the API Gateway using Spring Cloud Circuit Breaker with Resilience4J. The circuit breaker protects the system from cascading failures by monitoring service calls and providing fallback responses when services are unavailable.

## Features

- **Service-specific circuit breakers** for auth-service, restaurant-service, and zomato-service
- **Configurable thresholds** for failure rates, slow calls, and timeout durations
- **Automatic fallback responses** when services are unavailable
- **Retry mechanisms** with exponential backoff
- **Comprehensive monitoring** and health check endpoints
- **Real-time metrics** and circuit breaker state monitoring

## Circuit Breaker States

1. **CLOSED**: Normal operation, requests are allowed through
2. **OPEN**: Service is failing, requests are blocked and fallback is triggered
3. **HALF_OPEN**: Testing if service has recovered, limited requests are allowed

## Configuration

### Service-Specific Settings

#### Auth Service
- **Sliding Window Size**: 10 requests
- **Failure Rate Threshold**: 60%
- **Slow Call Threshold**: 60%
- **Slow Call Duration**: 1000ms
- **Wait Duration in Open State**: 20s
- **Timeout Duration**: 2s

#### Restaurant Service
- **Sliding Window Size**: 15 requests
- **Failure Rate Threshold**: 55%
- **Slow Call Threshold**: 55%
- **Slow Call Duration**: 1500ms
- **Wait Duration in Open State**: 25s
- **Timeout Duration**: 2.5s

#### Zomato Service
- **Sliding Window Size**: 25 requests
- **Failure Rate Threshold**: 45%
- **Slow Call Threshold**: 45%
- **Slow Call Duration**: 3000ms
- **Wait Duration in Open State**: 35s
- **Timeout Duration**: 4s

## API Endpoints

### Service Routes with Circuit Breaker Protection

1. **Auth Service**: `/auth/**`
   - Circuit breaker: `auth-service`
   - Fallback: `/fallback/auth-service`

2. **Restaurant Service**: `/orders/**`
   - Circuit breaker: `restaurant-service`
   - Fallback: `/fallback/restaurant-service`

3. **Zomato Service**: `/zomato/orders/**`
   - Circuit breaker: `zomato-service`
   - Fallback: `/fallback/zomato-service`

### Monitoring Endpoints

1. **Overall Health**: `GET /actuator/circuitbreaker/health`
   - Returns overall circuit breaker health status
   - Shows state and metrics for all circuit breakers

2. **Service Details**: `GET /actuator/circuitbreaker/details/{service-name}`
   - Detailed metrics for specific circuit breaker
   - Configuration and current state information

3. **Circuit Breaker Events**: `GET /actuator/circuitbreaker/events/{service-name}`
   - Event monitoring for specific circuit breaker

4. **Actuator Endpoints**: `/actuator/health`, `/actuator/circuitbreakers`
   - Spring Boot Actuator endpoints for health and circuit breaker monitoring

## Fallback Responses

When a circuit breaker is open, the system returns structured error responses:

```json
{
  "error": "Service temporarily unavailable",
  "message": "Descriptive message about the service unavailability",
  "timestamp": "2024-01-01T12:00:00",
  "status": 503,
  "service": "service-name"
}
```

## Testing the Circuit Breaker

### Manual Testing

1. **Simulate Service Failure**:
   - Stop one of the downstream services
   - Make multiple requests to trigger the circuit breaker
   - Observe the fallback response

2. **Monitor Circuit Breaker State**:
   ```bash
   curl http://localhost:8086/actuator/circuitbreaker/health
   ```

3. **Check Specific Service Details**:
   ```bash
   curl http://localhost:8086/actuator/circuitbreaker/details/auth-service
   ```

### Load Testing

Use tools like Apache JMeter or curl to simulate high load:

```bash
# Simulate multiple requests
for i in {1..50}; do
  curl -X POST http://localhost:8086/auth/login \
    -H "Content-Type: application/json" \
    -d '{"username":"test","password":"test"}'
  sleep 0.1
done
```

## Monitoring and Alerting

### Metrics Available

- **Failure Rate**: Percentage of failed calls
- **Slow Call Rate**: Percentage of slow calls
- **Number of Successful Calls**: Count of successful requests
- **Number of Failed Calls**: Count of failed requests
- **Number of Slow Calls**: Count of slow requests
- **Number of Not Permitted Calls**: Count of blocked requests

### Health Check Integration

The circuit breaker health is integrated with Spring Boot Actuator:
- Circuit breaker states are exposed in `/actuator/health`
- Individual circuit breaker metrics are available
- Can be integrated with monitoring systems like Prometheus, Grafana

### Logging

Circuit breaker events are logged with the following levels:
- **DEBUG**: Detailed circuit breaker state changes
- **INFO**: Circuit breaker transitions (OPEN/CLOSED/HALF_OPEN)
- **WARN**: High failure rates or slow calls
- **ERROR**: Circuit breaker configuration issues

## Best Practices

1. **Threshold Tuning**: Adjust thresholds based on service characteristics
2. **Fallback Design**: Provide meaningful fallback responses
3. **Monitoring**: Continuously monitor circuit breaker metrics
4. **Testing**: Regularly test circuit breaker behavior
5. **Documentation**: Keep configuration changes documented

## Configuration Customization

Circuit breaker settings can be customized in `application.yml`:

```yaml
resilience4j:
  circuitbreaker:
    instances:
      your-service:
        register-health-indicator: true
        sliding-window-size: 20
        failure-rate-threshold: 50
        wait-duration-in-open-state: 30s
        # ... other configuration
```

## Troubleshooting

### Common Issues

1. **Circuit Breaker Not Triggering**:
   - Check failure rate thresholds
   - Verify sliding window size is appropriate
   - Ensure proper exception handling

2. **Frequent Circuit Breaker Opening**:
   - Review threshold configurations
   - Check downstream service health
   - Analyze slow call duration settings

3. **Fallback Not Working**:
   - Verify fallback URI configuration
   - Check fallback controller mapping
   - Ensure proper error handling

### Debug Mode

Enable debug logging for detailed circuit breaker information:

```yaml
logging:
  level:
    io.github.resilience4j: DEBUG
    org.springframework.cloud.circuitbreaker: DEBUG
```

## Future Enhancements

- Integration with distributed tracing (Sleuth/Zipkin)
- Custom metrics and alerting
- Circuit breaker dashboard
- A/B testing with circuit breaker states
- Integration with service mesh (Istio)

## Dependencies

The implementation uses the following key dependencies:
- Spring Cloud Circuit Breaker
- Resilience4J
- Spring Boot Actuator
- Spring Cloud Gateway

For more information, refer to the official documentation:
- [Spring Cloud Circuit Breaker](https://spring.io/projects/spring-cloud-circuitbreaker)
- [Resilience4J](https://resilience4j.readme.io/docs)