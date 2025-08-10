# Read Me First

The following was discovered as part of building this project:

* The original package name 'com.ragukvn.spring-batch-data-manager' is invalid and this project uses '
  com.ragukvn.data_manager' instead.
* I have avoided the fault tolerance features of batch processing, such as retry and skip, to keep the project simple
  and banking transactions are sensitive to these features.
* The project uses an in-memory H2 database for demonstration purposes, but it can be easily configured to use other
  databases like MySQL or PostgreSQL by changing the `application.yml` file.
* Assuming that the input file is in CSV format, and the data types are valid. Only file input header row validation is
  implemented, and no other validations are performed on the input data.
* If validation needed can implemente a custom data processor to validate the input data.


# Getting Started

* In this project, we use the [Spring Boot Maven Plugin](https://docs.spring.io/spring-boot/3.5.4/maven-plugin) to build
  and run the application.
* In this project, we use Java 21 as the target version.
* The project is configured to use the [Spring Batch](https://docs.spring.io/spring-boot/3.5.4/how-to/batch.html)
  framework for batch processing.
* The project is set up to
  use [Spring Data JPA](https://docs.spring.io/spring-boot/3.5.4/reference/data/sql.html#data.sql.jpa-and-spring-data)
  for database interactions.
* The project includes a RESTful web service
  using [Spring Web](https://docs.spring.io/spring-boot/3.5.4/reference/web/servlet.html).

### API Documentation
Access swagger UI at `http://localhost:8080/swagger-ui/index.html` to explore the API endpoints.
### Reference Documentation

For further reference, please consider the following sections: