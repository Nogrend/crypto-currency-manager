# Crypto Currency Manager REST API
This project implements a RESTful API for managing basic information about crypto currencies.
It allows users to perform CRUD operations on currency records and supports paging and sorting functionalities. 
Additionally, the project includes an in-memory database to store currency data temporarily.

## Features
REST Endpoints: Provides endpoints for adding, reading, updating, and deleting currency records.
In-Memory Database: Stores currency data during runtime.
Paging and Sorting: Supports paging and sorting for currency records.
Logging: Implements extensive logging using a standard log framework to track API usage.

````mermaid
classDiagram
direction BT
class CorsConfig {
  + CorsConfig() 
  - String[] allowedOrigins
  + addCorsMappings(CorsRegistry) void
}
class CreateCurrencyCommand {
  + CreateCurrencyCommand(UUID, String, String, Double, Double) 
  - UUID id
  - String ticker
  - String name
  - Double numberOfCoins
  - Double marketCapitalization
  + numberOfCoins() Double
  + marketCapitalization() Double
  + id() UUID
  + name() String
  + ticker() String
}
class CryptoCurrencyManagerApplication {
  + CryptoCurrencyManagerApplication() 
  + main(String[]) void
}
class Currency {
  + Currency(UUID, String, String, Double, Double) 
  + Currency() 
  - String ticker
  - Double marketCapitalization
  - UUID id
  - String name
  - Double numberOfCoins
  + setNumberOfCoins(Double) void
  + getName() String
  + getTicker() String
  + setId(UUID) void
  + getId() UUID
  + getNumberOfCoins() Double
  + setMarketCapitalization(Double) void
  + setName(String) void
  + getMarketCapitalization() Double
  + setTicker(String) void
}
class CurrencyController {
  + CurrencyController(CurrencyService) 
  - CurrencyService currencyService
  + updateCurrency(UUID, UpdateCurrencyCommand) void
  + deleteCurrency(UUID) void
  + getCurrencyById(UUID) CurrencyDTO
  + createCurrency(CreateCurrencyCommand) void
  + getCurrencies(int, int) Page~Currency~
}
class CurrencyDTO {
  + CurrencyDTO(UUID, String, String, Double, Double) 
  - Double numberOfCoins
  - Double marketCapitalization
  - UUID id
  - String name
  - String ticker
  + id() UUID
  + ticker() String
  + numberOfCoins() Double
  + marketCapitalization() Double
  + name() String
}
class CurrencyNotFoundException {
  + CurrencyNotFoundException(UUID) 
}
class CurrencyRepository {
<<Interface>>

}
class CurrencyService {
  + CurrencyService(CurrencyRepository) 
  + CurrencyRepository currencyRepository
  + updateCurrencyById(UUID, UpdateCurrencyCommand) void
  + getCurrencyById(UUID) Currency
  + createCurrency(CreateCurrencyCommand) void
  - findByIdOrThrow(UUID) Currency
  + getCurrenciesAsPageable(Pageable) Page~Currency~
  + deleteCurrencyById(UUID) void
}
class GlobalExceptionHandler {
  + GlobalExceptionHandler() 
  + handleCurrencyNotFound(CurrencyNotFoundException) ResponseEntity~String~
  ~ onConstraintValidationException(ConstraintViolationException) ValidationErrorResponse
}
class LoggingFilter {
  + LoggingFilter() 
  - String messageTemplate
  - Logger logger
  - FileHandler fileHandler
  + doFilter(ServletRequest, ServletResponse, FilterChain) void
  + destroy() void
  + init(FilterConfig) void
}
class UpdateCurrencyCommand {
  + UpdateCurrencyCommand(String, String, Double, Double) 
  - Double marketCapitalization
  - String ticker
  - String name
  - Double numberOfCoins
  + ticker() String
  + name() String
  + numberOfCoins() Double
  + marketCapitalization() Double
}
class ValidationErrorResponse {
  + ValidationErrorResponse() 
  - List~Violation~ violations
  + getViolations() List~Violation~
}
class Violation {
  + Violation(String, String) 
  - String fieldName
  - String message
  + fieldName() String
  + message() String
}

CurrencyController  ..>  CurrencyDTO : «create»
CurrencyController "1" *--> "currencyService 1" CurrencyService 
CurrencyService  ..>  Currency : «create»
CurrencyService  ..>  CurrencyNotFoundException : «create»
CurrencyService "1" *--> "currencyRepository 1" CurrencyRepository 
GlobalExceptionHandler  ..>  ValidationErrorResponse : «create»
GlobalExceptionHandler  ..>  Violation : «create»
ValidationErrorResponse "1" *--> "violations *" Violation 

````