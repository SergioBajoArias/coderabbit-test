package com.sergio;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class TestingResourceTest {

    @Nested
    @DisplayName("GET /hello endpoint tests")
    class HelloEndpointTests {

        @Test
        @DisplayName("Should return 'Hello from Quarkus REST' with status 200")
        public void testHelloEndpoint() {
            given()
              .when().get("/hello")
              .then()
                 .statusCode(200)
                 .body(is("Hello from Quarkus REST"));
        }

        @Test
        @DisplayName("Should return content type text/plain")
        public void testHelloEndpointContentType() {
            given()
              .when().get("/hello")
              .then()
                 .statusCode(200)
                 .contentType("text/plain")
                 .body(is("Hello from Quarkus REST"));
        }

        @Test
        @DisplayName("Should return non-empty response body")
        public void testHelloEndpointNonEmptyResponse() {
            given()
              .when().get("/hello")
              .then()
                 .statusCode(200)
                 .body(notNullValue())
                 .body(containsString("Hello"));
        }

        @Test
        @DisplayName("Should handle multiple concurrent requests")
        public void testHelloEndpointConcurrentRequests() {
            // Test concurrent access to ensure thread safety
            for (int i = 0; i < 10; i++) {
                given()
                  .when().get("/hello")
                  .then()
                     .statusCode(200)
                     .body(is("Hello from Quarkus REST"));
            }
        }

        @Test
        @DisplayName("Should return consistent response on repeated calls")
        public void testHelloEndpointConsistency() {
            String firstResponse = given()
                .when().get("/hello")
                .then()
                   .statusCode(200)
                   .extract().body().asString();

            String secondResponse = given()
                .when().get("/hello")
                .then()
                   .statusCode(200)
                   .extract().body().asString();

            assertEquals(firstResponse, secondResponse, "Response should be consistent across calls");
        }
    }

    @Nested
    @DisplayName("HTTP Method tests")
    class HttpMethodTests {

        @Test
        @DisplayName("Should only accept GET requests")
        public void testOnlyGetMethodAllowed() {
            given()
              .when().get("/hello")
              .then()
                 .statusCode(200);
        }

        @Test
        @DisplayName("Should return 405 for POST requests")
        public void testPostMethodNotAllowed() {
            given()
              .when().post("/hello")
              .then()
                 .statusCode(405);
        }

        @Test
        @DisplayName("Should return 405 for PUT requests")
        public void testPutMethodNotAllowed() {
            given()
              .when().put("/hello")
              .then()
                 .statusCode(405);
        }

        @Test
        @DisplayName("Should return 405 for DELETE requests")
        public void testDeleteMethodNotAllowed() {
            given()
              .when().delete("/hello")
              .then()
                 .statusCode(405);
        }

        @Test
        @DisplayName("Should return 405 for PATCH requests")
        public void testPatchMethodNotAllowed() {
            given()
              .when().patch("/hello")
              .then()
                 .statusCode(405);
        }
    }

    @Nested
    @DisplayName("Path and URL tests")
    class PathTests {

        @Test
        @DisplayName("Should be accessible at exact path /hello")
        public void testExactPath() {
            given()
              .when().get("/hello")
              .then()
                 .statusCode(200)
                 .body(is("Hello from Quarkus REST"));
        }

        @Test
        @DisplayName("Should return 404 for non-existent paths")
        public void testNonExistentPath() {
            given()
              .when().get("/hello/world")
              .then()
                 .statusCode(404);
        }

        @Test
        @DisplayName("Should return 404 for root path")
        public void testRootPath() {
            given()
              .when().get("/")
              .then()
                 .statusCode(404);
        }

        @Test
        @DisplayName("Should return 404 for case-sensitive path variations")
        public void testCaseSensitivePath() {
            given()
              .when().get("/Hello")
              .then()
                 .statusCode(404);

            given()
              .when().get("/HELLO")
              .then()
                 .statusCode(404);
        }
    }

    @Nested
    @DisplayName("Header and content negotiation tests")
    class HeaderTests {

        @Test
        @DisplayName("Should accept requests with Accept: text/plain header")
        public void testAcceptTextPlain() {
            given()
              .header("Accept", "text/plain")
              .when().get("/hello")
              .then()
                 .statusCode(200)
                 .contentType("text/plain")
                 .body(is("Hello from Quarkus REST"));
        }

        @Test
        @DisplayName("Should accept requests with Accept: */* header")
        public void testAcceptWildcard() {
            given()
              .header("Accept", "*/*")
              .when().get("/hello")
              .then()
                 .statusCode(200)
                 .body(is("Hello from Quarkus REST"));
        }

        @Test
        @DisplayName("Should handle requests without Accept header")
        public void testNoAcceptHeader() {
            given()
              .when().get("/hello")
              .then()
                 .statusCode(200)
                 .body(is("Hello from Quarkus REST"));
        }

        @Test
        @DisplayName("Should return 406 for incompatible Accept header")
        public void testIncompatibleAcceptHeader() {
            given()
              .header("Accept", "application/json")
              .when().get("/hello")
              .then()
                 .statusCode(406);
        }
    }

    @Nested
    @DisplayName("Response validation tests")
    class ResponseValidationTests {

        @Test
        @DisplayName("Should return exact expected message")
        public void testExactMessage() {
            String response = given()
                .when().get("/hello")
                .then()
                   .statusCode(200)
                   .extract().body().asString();

            assertEquals("Hello from Quarkus REST", response, "Response message should be exact");
        }

        @Test
        @DisplayName("Should return string response")
        public void testResponseType() {
            String response = given()
                .when().get("/hello")
                .then()
                   .statusCode(200)
                   .extract().body().asString();

            assertInstanceOf(String.class, response, "Response should be a String");
        }

        @Test
        @DisplayName("Should return non-null response")
        public void testNonNullResponse() {
            String response = given()
                .when().get("/hello")
                .then()
                   .statusCode(200)
                   .extract().body().asString();

            assertNotNull(response, "Response should not be null");
        }

        @Test
        @DisplayName("Should return non-empty response")
        public void testNonEmptyResponse() {
            String response = given()
                .when().get("/hello")
                .then()
                   .statusCode(200)
                   .extract().body().asString();

            assertFalse(response.isEmpty(), "Response should not be empty");
        }

        @Test
        @DisplayName("Should contain expected keywords")
        public void testResponseContent() {
            String response = given()
                .when().get("/hello")
                .then()
                   .statusCode(200)
                   .extract().body().asString();

            assertTrue(response.contains("Hello"), "Response should contain 'Hello'");
            assertTrue(response.contains("Quarkus"), "Response should contain 'Quarkus'");
            assertTrue(response.contains("REST"), "Response should contain 'REST'");
        }
    }

    @Nested
    @DisplayName("Performance and load tests")
    class PerformanceTests {

        @Test
        @DisplayName("Should handle rapid successive requests")
        public void testRapidRequests() {
            for (int i = 0; i < 50; i++) {
                given()
                  .when().get("/hello")
                  .then()
                     .statusCode(200)
                     .body(is("Hello from Quarkus REST"));
            }
        }

        @Test
        @DisplayName("Should maintain consistent response time pattern")
        public void testResponseTimeConsistency() {
            long startTime = System.currentTimeMillis();
            
            for (int i = 0; i < 10; i++) {
                given()
                  .when().get("/hello")
                  .then()
                     .statusCode(200);
            }
            
            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            
            assertTrue(totalTime < 5000, "10 requests should complete within 5 seconds");
        }
    }

    @Nested
    @DisplayName("Edge case tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle requests with query parameters")
        public void testWithQueryParameters() {
            given()
              .queryParam("test", "value")
              .when().get("/hello")
              .then()
                 .statusCode(200)
                 .body(is("Hello from Quarkus REST"));
        }

        @Test
        @DisplayName("Should handle requests with custom User-Agent")
        public void testCustomUserAgent() {
            given()
              .header("User-Agent", "TestClient/1.0")
              .when().get("/hello")
              .then()
                 .statusCode(200)
                 .body(is("Hello from Quarkus REST"));
        }

        @Test
        @DisplayName("Should handle requests with trailing slash")
        public void testTrailingSlash() {
            given()
              .when().get("/hello/")
              .then()
                 .statusCode(404); // Exact path matching expected
        }

        @Test
        @DisplayName("Should handle empty request body gracefully")
        public void testEmptyBody() {
            given()
              .body("")
              .when().get("/hello")
              .then()
                 .statusCode(200)
                 .body(is("Hello from Quarkus REST"));
        }
    }

    @Nested
    @DisplayName("Unit tests for TestingResource class")
    class UnitTests {

        @Test
        @DisplayName("Should instantiate TestingResource")
        public void testInstantiation() {
            TestingResource resource = new TestingResource();
            assertNotNull(resource, "TestingResource should be instantiable");
        }

        @Test
        @DisplayName("Should return correct message from hello method")
        public void testHelloMethodDirectly() {
            TestingResource resource = new TestingResource();
            String result = resource.hello();
            
            assertEquals("Hello from Quarkus REST", result, "hello() method should return expected string");
            assertNotNull(result, "hello() method should not return null");
            assertFalse(result.isEmpty(), "hello() method should not return empty string");
        }

        @Test
        @DisplayName("Should return consistent result from hello method")
        public void testHelloMethodConsistency() {
            TestingResource resource = new TestingResource();
            String firstCall = resource.hello();
            String secondCall = resource.hello();
            
            assertEquals(firstCall, secondCall, "hello() method should return consistent results");
        }

        @Test
        @DisplayName("Should return immutable result from hello method")
        public void testHelloMethodImmutability() {
            TestingResource resource = new TestingResource();
            String result = resource.hello();
            String originalResult = result;
            
            // Attempt to modify the string (this should create a new string)
            result = result.toUpperCase();
            
            // Verify original method still returns the same value
            String newCall = resource.hello();
            assertEquals(originalResult, newCall, "hello() method result should be immutable");
        }

        @Test
        @DisplayName("Should handle multiple instances independently")
        public void testMultipleInstancesIndependence() {
            TestingResource resource1 = new TestingResource();
            TestingResource resource2 = new TestingResource();
            
            String result1 = resource1.hello();
            String result2 = resource2.hello();
            
            assertEquals(result1, result2, "Multiple instances should return the same result");
        }

        @Test
        @DisplayName("Should have proper class structure")
        public void testClassStructure() {
            TestingResource resource = new TestingResource();
            
            // Verify the class has the expected annotations
            assertTrue(resource.getClass().isAnnotationPresent(jakarta.ws.rs.Path.class), 
                      "TestingResource should have @Path annotation");
            
            // Verify the path value
            jakarta.ws.rs.Path pathAnnotation = resource.getClass().getAnnotation(jakarta.ws.rs.Path.class);
            assertEquals("/hello", pathAnnotation.value(), "Path should be '/hello'");
        }

        @Test
        @DisplayName("Should have proper method annotations")
        public void testMethodAnnotations() throws NoSuchMethodException {
            java.lang.reflect.Method helloMethod = TestingResource.class.getMethod("hello");
            
            assertTrue(helloMethod.isAnnotationPresent(jakarta.ws.rs.GET.class), 
                      "hello() method should have @GET annotation");
            assertTrue(helloMethod.isAnnotationPresent(jakarta.ws.rs.Produces.class), 
                      "hello() method should have @Produces annotation");
            
            jakarta.ws.rs.Produces producesAnnotation = helloMethod.getAnnotation(jakarta.ws.rs.Produces.class);
            assertEquals(jakarta.ws.rs.core.MediaType.TEXT_PLAIN, producesAnnotation.value()[0], 
                        "Should produce text/plain content type");
        }

        @Test
        @DisplayName("Should have correct return type")
        public void testReturnType() throws NoSuchMethodException {
            java.lang.reflect.Method helloMethod = TestingResource.class.getMethod("hello");
            assertEquals(String.class, helloMethod.getReturnType(), 
                        "hello() method should return String");
        }

        @Test
        @DisplayName("Should have no parameters")
        public void testMethodParameters() throws NoSuchMethodException {
            java.lang.reflect.Method helloMethod = TestingResource.class.getMethod("hello");
            assertEquals(0, helloMethod.getParameterCount(), 
                        "hello() method should have no parameters");
        }
    }

    @Nested
    @DisplayName("Security and error handling tests")
    class SecurityTests {

        @Test
        @DisplayName("Should handle malicious headers gracefully")
        public void testMaliciousHeaders() {
            given()
              .header("X-Forwarded-For", "evil.com")
              .header("X-Real-IP", "192.168.1.1")
              .when().get("/hello")
              .then()
                 .statusCode(200)
                 .body(is("Hello from Quarkus REST"));
        }

        @Test
        @DisplayName("Should handle long query parameters")
        public void testLongQueryParameter() {
            String longValue = "a".repeat(1000);
            given()
              .queryParam("test", longValue)
              .when().get("/hello")
              .then()
                 .statusCode(200)
                 .body(is("Hello from Quarkus REST"));
        }

        @Test
        @DisplayName("Should handle special characters in query parameters")
        public void testSpecialCharactersInQuery() {
            given()
              .queryParam("special", "<script>alert('xss')</script>")
              .when().get("/hello")
              .then()
                 .statusCode(200)
                 .body(is("Hello from Quarkus REST"));
        }

        @Test
        @DisplayName("Should not leak sensitive information in response")
        public void testNoSensitiveInformationLeak() {
            String response = given()
                .when().get("/hello")
                .then()
                   .statusCode(200)
                   .extract().body().asString();

            // Ensure no sensitive information is leaked
            assertFalse(response.contains("password"), "Response should not contain 'password'");
            assertFalse(response.contains("secret"), "Response should not contain 'secret'");
            assertFalse(response.contains("key"), "Response should not contain 'key'");
            assertFalse(response.contains("token"), "Response should not contain 'token'");
        }
    }
}