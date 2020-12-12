# Getting Started

### Tool set
* Java 8
* Maven 3.3.9
* Spring Boot 2.2.0
* MySQL 5.7 above. Supports 8.0

### DB migration
* `mvn -Dflyway.configFiles=config/application-${env}.yml flyway:info`
* `mvn -Dflyway.configFiles=config/application-${env}.yml flyway:clean`
* `mvn -Dflyway.configFiles=config/application-${env}.yml flyway:repair`
* `mvn -Dflyway.configFiles=config/application-${env}.yml flyway:migrate`

###  Generate sources (for querydsl and protobuf)
* run ```mvn generate-sources```
* It will create class files required by querydsl inside the folder ```target/generated-sources```.
* Please note that the developer may need to refresh the project in their IDE to view the generated classes.

### Run tests
* unit tests: `mvn test`
* integration tests: `mvn verify -Dspring.profiles.active=${env}`

### Run the application
* For local development: `mvn spring-boot:run -Dspring-boot.run.profiles=${env}`
* In servers: `java -jar -Dspring.profiles.active=${env} ${jar-file-path}`

## Featues

- [x] Env based external config
- [x] Database migration 
- [x] Swagger docs
- [x] Repository
- [x] Common error handling using json schema
- [x] Common validation and errors
- [x] Multi language
- [x] Json logger with multitenency
- [x] Makefile for commands
- [x] Unit Test
- [x] Integration Test using cucumber
- [x] Mock server integration 
- [x] Different mods of operation ( test, non db, production)
- [X] Modular and version support for api
- [ ] Docker
- [ ] Docker Compose
- [ ] Kubernetes
- [ ] Share library across service


#### Packages or Modules
* API versioning is done at package/module level.
* Controller, service, repository and DTO classes are placed in the respective package/module.
* Controller does request validation and DTO creation instead of service object.
* Service methods contain only the business logic, and it usually returns the resource.
* Don't use the common repository directly inside the service class, instead use the
module specific repository (inside which common repository may be used).

#### Autowiring
* Use constructor autowiring always. Refer [this](https://dzone.com/articles/spring-di-patterns-the-good-the-bad-and-the-ugly)

#### Utils and Helpers
* Utils are functionalities which are common throughout the application and is not
related to any specific business logic.  
Eg: Date utilities, HTTP utilities etc.
* Helpers contain business logics shared across the application. Helper classes
can be placed in common module. But if version specific helpers are required, then it
should be subclassed and kept in the respective module.  
Eg: Subscription checking can be moved to SubscriptionHelper class, since it may be used
at multiple places across the application.

#### Checkstyle
* Checkstyle validation is integrated as part of the build process.
* Build will fail if any of the checkstyle rules are violated.
* To check for violations, run `mvn checkstyle:checkstyle`

#### API Docs
* Default api docs available in `http://localhost:8081/swagger-ui.html`.
* Sample API Docs annotations for OpenAPI Spec

```java
     @Operation(summary = "Save Product")
     @ApiResponses(value = {
             @ApiResponse(responseCode = "200", description = "Product saved",
                     content = {@Content(mediaType = "application/json",
                             schema = @Schema(implementation = Product.class))}),
             @ApiResponse(responseCode = "400", description = "Invalid product supplied",
                     content = {@Content(mediaType = "application/json",
                             schema = @Schema(implementation = ErrorData.class))}),
             @ApiResponse(responseCode = "401", description = "Access denied",
                     content = @Content)})
```

#### Logs
* All logs at tenant level.
* Audit log - Any activity happens to the resource.
* Error log - All the 400 and above errors should be logged here
* Info log - Informational and debug logs 
* Application logs - Normal application logs include request and response logs

* Sample log structure

```
logs
├── archived
│   ├── micro-core-application-2020-06-28.0.log
│   ├── micro-core-application-29-06-2020.0.log
│   ├── micro-core-audit-2020-06-28.0.log
│   ├── micro-core-error-2020-06-28.0.log
│   ├── micro-core-info-2020-06-28.0.log
│   ├── micro-core-application-2020-06-28.0.log.tenant1
│   ├── micro-core-audit-2020-06-28.0.log.tenant1
│   ├── micro-core-error-2020-06-28.0.log.tenant1
│   └── micro-core-info-2020-06-28.0.log.tenant1
├── micro-core-application.log
├── micro-core-audit.log
├── micro-core-error.log
├── micro-core-info.log
├── micro-core-application.log.tenant1
├── micro-core-audit.log.tenant1
├── micro-core-error.log.tenant1
└── micro-core-info.log.tenant1
```

### Run integration test

```
mvn failsafe:integration-test -Dgroups="Issue-100" 
mvn failsafe:integration-test -Dgroups="saveProductWithExistingCodeShouldReturn409"
mvn test -Dgroups="Issue-100" 
mvn clean package -DskipTests; mvn failsafe:integration-test -Dgroups="saveProductWithExistingCodeShouldReturn409"
mvn surefire:test -Dtest=ProductTestSuiteCucumber
mvn test -Dtest=ProductScenarioTest  
```
