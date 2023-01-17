<p align="center">
  <a href="" rel="noopener">
 <img src="https://www.themeetgroup.com/wp-content/themes/wfx-girder/assets/images/logo_2x.png" alt="Project logo"></a>
</p>

<h3 align="center">Java Code Challenge</h3>


---

<p align="center"> Spring boot-based app that serves two separate, unrelated pieces of functionality.   
    <br> 
    1) An in-memory stack | 2) An in-memory key-value store with TTL
</p>

## 📝 Table of Contents
- [About](#about)
- [Getting Started](#getting_started)
- [Usage](#usage)
- [Built Using](#built_using)

## 🧐 About <a name = "about"></a>
This project is an implementation that matches with all the code challenge requirements that were sent in previous contact.

### General

#### The package structure
Before going into details about the implementation, the package structure is explained as it follows:
```
java/
├─ com.tmg.codingchallenge (base package)/
│  ├─ scope-level-package (global;stack;cache)/
│  │  ├─ api/
│  │  │  ├─  {REST Controllers and respective Swagger doc}
│  │  ├─ dto/
│  │  │  ├─  {Classes meant for data transfer only}
│  │  ├─ job/
│  │  │  ├─ {Classes with schedulers}
│  │  ├─ model/
│  │  │  ├─ {Domain related classes}
│  │  ├─ repository/
│  │  │  ├─ {Classes that represents the data layer}
│  │  ├─ service/
│  │  │  ├─ {Classes that wrap up domain specific implementations}
│  │  ├─ config/
│  │  │  ├─ {Classes used to configure beans or tools}
│  │  ├─ converter/
│  │  │  ├─ {Classes for non-model related object convertion}
│  │  ├─ helper/
│  │  │  ├─ {Classes with static methods meant for application specific functionality}
│  │  ├─ util/
│  │  │  ├─ {Classes with static methods meant for generic functionality}
```
> Note: in the `scope-level-package` there is a specific package for each project but there is another package as well. The third package `global` was used to centralize global settings that are used in both challenges such as the **Swagger configuration class** and a **Global Exception Handler**.

#### Global Exception Handler and Controller response pattern for BAD REQUESTS
The Global Exception Handler is meant to define a pattern for HTTP bad responses, such as `BAD REQUEST`, `INTERNAL SERVER ERROR` and so on.

1. Default response format for bad requests
```json
{
  "timestamp": "2023-01-17T17:37:39.663376",
  "argumentsErrors": [
    {
      "field": "ttl",
      "message": "When informed, TTL value must be a greater than ZERO!"
    }
  ],
  "detail": "uri=/cache"
}
```

2. Generic response for unhandled exceptions
```json
{
  "timestamp": "2023-01-17T17:43:17.470364",
  "message": "An unexpected error happened. Please contact support",
  "detail": "uri=/cache"
}
```
> When an unhandled exception happens the Exception handler just log the exception message and it's stack trace but in true applications it could do something more to let the development team know about the problem that happened. 

TODO - Explain flow (controller -> service -> repository and job -> service -> repository)

### Stack Challenge
For the challenge I decided to implement a custom stack class (`com.tmg.codingchallenge.stackchallenge.model.CustomStack`), but in real applications I would rather use
  `java.utils.Stack` if it's suitable for the application needs.
> The package `com.tmg.codingchallenge.stackchallenge` contains the classes for this part of the test.

### In-memory key-value storage with TTL Challenge

For the matters of this test, I decided to implement an active expiring storage, but in real applications I would take
into consideration the usage it's intended for the feature in order to decide for an active or passive approach, and I would also prioritize using a existent tool if it matches the applications needs.

TODO explain more (scheduler, repository data structure...)

> The package `com.tmg.codingchallenge.cachechallenge` contains the classes for this part of the test.

## 🏁 Getting Started <a name = "getting_started"></a>
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites
Before running this application in a local environment, make sure you have both [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) and [Maven](https://maven.apache.org/) installed on your machine.

### Running

1. First download this project manually or clone it locally using Git and access the project's directory on your device.

```
git clone https://github.com/matheusaugsschmitz/PYYNE-TMG-Challenge.git
cd .\PYYNE-TMG-Challenge\
```

2. Then compile the project using the following Maven command

```
mvn install -DskipTests
```
> `-DskipTests` is used for avoid running automated tests after building the project, if you want to run the tests as well just remove this part from the command and execute it

3. And start the Spring Boot application
```
mvn spring-boot:run
```

## 🔧 Running the tests <a name = "tests"></a>

- Option 1: Run after Maven Install command
```
mvn test
```

- Option 2: Running along with Maven Install command
```
mvn install
```

### Break down into end-to-end tests
The end-to-end tests are meant to call the REST controllers and validate in the respective repositories if the data was added/modified as it was supposed to.

The end-to-end tests are located on the package `com.tmg.codingchallenge.endtoendtest` in the test directory.

### And integration tests
The integration tests are meant to execute the whole application without mocking anything, but validating the REST controller response instead of the final data structure.

The integrations tests are located on the package `com.tmg.codingchallenge.integrationtest` in the test directory.

## 🎈 Usage <a name="usage"></a>
Once you get the application running you can send requests to the REST controllers using Postman, Insomnia or any other tool that allows you to make HTTP requests.

This application have a Swagger page configured with the endpoints documentation that allows you to use them via web browser as well.

### Swagger URL
> http://localhost:8100/swagger-ui/index.html#/

## ⛏️ Built Using <a name = "built_using"></a>
- [Java 17](https://docs.oracle.com/en/java/javase/17/docs/api/index.html)
- [Spring Boot 3.0.1](https://docs.spring.io/spring-boot/docs/3.0.x/api/)
- [Lombok 1.18.24](https://projectlombok.org/features/)
- [OpenAPI 3 with SpringDoc OpenAPI v2](https://springdoc.org/v2/)
- [JUnit 5.9.1](https://junit.org/junit5/docs/5.9.1/api/index.html)
- [Mockito 4.8.1](https://javadoc.io/doc/org.mockito/mockito-core/4.8.1/org/mockito/Mockito.html)
