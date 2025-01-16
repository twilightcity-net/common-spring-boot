# Common SpringBoot Library

A shared library providing common functionality for building SpringBoot applications, with focus on persistence operations and REST service support.

## Core Features

### 1. Persistence Support

#### Object Mapping
- Generic API-to-Entity mapping via `ApiEntityMapper`
- Customizable mapping configurations through Dozer
- Built-in support for common Java types:
  - UUID
  - LocalDate
  - LocalDateTime
  - ZonedDateTime

#### Transaction Management
- Declarative transaction management through Spring configurations
- Support for different transaction propagation levels:
  - `RequiredTransactor`: Standard transaction behavior
  - `RequiresNewTransactor`: Creates new transaction
  - `MandatoryTransactor`: Requires existing transaction
  - `SupportsTransactor`: Optional transaction support

### 2. REST Service Support

#### Exception Handling
- Standardized exception hierarchy for common HTTP status codes:
  - `BadRequestException` (400)
  - `NotAuthorizedException` (401)
  - `ForbiddenException` (403)
  - `NotFoundException` (404)
  - `ConflictException` (409)
  - `InternalServerException` (500)
- Structured error responses via `ErrorEntity`
- Custom error code support
- Client-side exception translation

#### Logging Framework
- Request/Response logging with configurable detail levels
- MDC (Mapped Diagnostic Context) support
- Request ID tracking and correlation
- Configurable header logging with security considerations
- Content type-aware payload logging
- Size-limited payload logging
- Common Logback configuration
