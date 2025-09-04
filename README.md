# Prescription Service

A Spring Boot application that provides consultation APIs for assessing patient eligibility for medication prescriptions. This service handles the consultation phase of a telemedicine platform, allowing patients to answer health-related questions and receive eligibility assessments.

## Features

- **Question Retrieval**: REST API to fetch consultation questions
- **Answer Processing**: REST API to submit patient answers and receive eligibility assessment

## Getting Started

### Prerequisites

- Java 21 or later

### Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Run the application:
   ```bash
   ./gradlew bootRun
   ```
4. The application will start on `http://localhost:8080`

### Running Tests

Execute all tests:
```bash
./gradlew test
```

View test report:
```bash
./gradlew test --info
```

## API Documentation

### Base URL
```
http://localhost:8080/api/consultations
```

### Endpoints

#### 1. Get Consultation Questions

Retrieves all available consultation questions for a specific product and creates a new consultation session.

```http
GET /api/consultations/{productId}/questions
```

**Path Parameters:**
- `productId` (string, required): The product identifier (e.g., "pear-allergy")

**Response:**
```json
{
  "consultationId": "uuid-string",
  "questions": [
    {
      "id": "Q1",
      "text": "Do you experience nose itchiness when near pears?",
      "type": "YES_NO",
      "required": true
    }
  ]
}
```

#### 2. Submit Consultation Answers

Processes patient answers and returns eligibility assessment.

```http
POST /api/consultations/{consultationId}/answers
```

**Request Body:**
```json
{
  "patientName": "John Doe",
  "dateOfBirth": "1990-01-01",
  "address": "123 Main Street, Test City, TC 12345",
  "answers": [
    {
      "questionId": "Q1",
      "value": "yes"
    },
    {
      "questionId": "Q2", 
      "value": "no"
    }
  ]
}
```

**Response:**
```json
{
  "consultationId": "uuid-string",
  "eligible": true,
  "message": "Based on your symptoms, you appear to be a good candidate for our pear allergy medication.",
  "status": "ELIGIBLE"
}
```

**Status Values:**
- `ELIGIBLE`: Patient is eligible for prescription
- `NOT_ELIGIBLE`: Patient is not eligible for prescription
- `REQUIRES_REVIEW`: Case requires doctor review

## Sample Usage

### Using cURL

1. **Get Questions:**
   ```bash
   curl -X GET http://localhost:8080/api/consultations/pear-allergy/questions
   ```

2. **Submit Answers:** Note, you will need to use the consultationId contained in the above GET request
   ```bash
   curl -X POST http://localhost:8080/api/consultations/{consultationId}/answers \
     -H "Content-Type: application/json" \
     -d '{
       "patientName": "John Doe",
       "dateOfBirth": "1990-01-01", 
       "address": "123 Main St, Test City, TC 12345",
       "answers": [
         {"questionId": "Q1", "value": "yes"},
         {"questionId": "Q2", "value": "yes"},
         {"questionId": "Q3", "value": "yes"},
         {"questionId": "Q4", "value": "no"},
         {"questionId": "Q5", "value": "no"}
       ]
     }'
   ```

## Architecture

## Eligibility Assessment Logic

The current eligibility assessment follows these rules:

1. **Adverse Reaction Check**: Patients with previous adverse reactions to allergy medication are automatically ineligible
2. **Symptom Severity Assessment**: 
   - 3+ severe symptoms (Q1, Q2, Q3 = "yes") → Eligible
   - 1-2 severe symptoms → Requires Review
   - 0 severe symptoms → Not Eligible

## TODO - Notes and Production Considerations

- **Repository Pattern**: Used interfaces with in-memory implementations to provide abstraction for potential database integration.
- **DTO Separation**: Clear separation between entities and DTOs to provide API stability and validation.
- **Simple Validation**: Validation annotations to ensure data integrity at the API boundary. Validation is quite simple, productionizing this would require making existing validation more exhaustive.
- **Error Handling**: Just a skeleton of what it might look like, production would likely need detailed error codes and internationalization
- **Eligibility Logic**: At the moment, the actual code to determine if a patient is eligible lives in the ConsultationService, in one method (assessEligibility). In a production-ready application, this would surely be a separate service, I imagine there would be scope for far more complex analysis. I'm aware of this, and I put all the rudimentary logic for eligibility assessment in this method just as a sample of some possible cases.
- **Patient Entity**: I created a patient entity since in reality we wouldn't have patient data being sent in every request, we would be more likely to just use the auth token identifying the patient and a separate call/query requesting patient data. I didn't mock that out due to time constraints, and it didn't seem to be a priority for the scope of the exercise, but I'm aware that it would need to change. 
- AnswerDto could possibly have a boolean field for yes/no questions. This would need some attention/data modeling.
- The response messages for eligibility requests should not be hardcoded. Especially in a scenario where internationalisation is a concern.
- I took the liberty of assuming that a request to get questions would be by product. As in, each product would have its own set of questions. This wasn't specifically stated, but it seemed sensible to me.
- The reason why I save a consultation when I return the questions to the patient is to account for the possibility of questions changing. As in, a user requests the questions, and before submitting the answers a new question is added for that product. That sort of thing. This is super debatable and would require clarifying requirements, but that was my reasoning.
- Note on the tests: ConsultationServiceTest is a showcase of what I usually would do for service-layer classes, unit tests that largely check the accuracy of the business logic. I generally would be more exhaustive than this in regards to testing edge cases/conditions. The ConsultationControllerTest is an example of how to test input validation, which I often find is the essential part of testing controllers. I do also usually create simple unit tests for controllers, but I find them less valuable than they are for service classes. Finally, ConsultationFlowIntegrationTest is a simple example of what I like to do in terms of testing exposed endpoints, I think there should always be integration tests directed towards testing a whole flow rather than specific conditions (which other tests cover). I would generally use testcontainers for this, with localstack for AWS dependencies.
- Support for different question types is hinted at in the code, but I didn't have time to expand, and it seems beyond the scope of this exercise.

If given more time, the following improvements should be implemented:

### Security & Compliance
- [ ] Granular permissions (ie, a customer should not have access to endpoints used for doctors)
- [ ] Implement audit logging and fields/attributes for compliance

### Data & Persistence
- [ ] Replace in-memory storage with proper database (ie PostgreSQL)
- [ ] Design and implement proper entity relationships and constraints
- [ ] Add soft deletes and audit trails (createdAt, updatedAt, deletedAt)

### Validation & Error Handling
- [ ] More comprehensive address validation (postal codes, format)
- [ ] Date of birth validation (age ranges, format)
- [ ] Enhanced error messages with internationalization
- [ ] Input sanitization to prevent injection attacks, particularly if having free text responses

### Monitoring & Observability
- [ ] Add structured logging with correlation IDs
- [ ] Implement health checks and metrics (Actuator enhancements)
- [ ] Tracing, performance monitoring and alerting

### Testing & Quality
- [ ] Enforce test coverage of 70 - 80% (really more of a company-wide discussion)
- [ ] Add performance/load testing
- [ ] Add contract testing for API versioning
- [ ] Expose OpenAPI spec

### Scalability & Performance
- [ ] Questions for a product are unlikely to change. Good use case for caching?
- [ ] It would be worth discussing whether the eligibility assessment should be asynchronous. As in, patient submits answers and waits for an e-mail. But I made a judgement call based on the requirements.
- [ ] Think of API versioning strategy
- [ ] Containerize
