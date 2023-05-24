# JavaTestAPI
Test Automation Framework - Java, Rest Assured, JUnit 5, Maven, and Allure Reporting
## Overview

This automation framework is built around several robust and reliable technologies including Java, Rest Assured library, JUnit 5, Maven, and Allure Reporting. It's designed for the testing of RESTful APIs and follows the Page Object Model (POM) pattern to provide a high level of maintainability and reusability.

## Key Features

 **Rest Assured:** Simplifies testing of HTTP based services. It's equipped to handle GET, POST, PUT, DELETE requests, and 
 includes additional features like validations, queries and path parameters.

 **JUnit 5:** Offers a foundation for launching testing frameworks on the JVM. It provides a rich set of assertions 
 to help you test expected versus actual results.

 **Maven:** Used for building and managing any Java-based project. It handles the project's build, reporting and 
 documentation from a central piece of information.

 **Allure Reporting:** An open-source framework designed to create test execution reports that are clear to 
 everyone in the team. It offers a wide range of features including test case history, test case retries, 
 integrations with popular test frameworks and much more.

 **Page Object Model (POM):** A design pattern that enhances test maintenance and reduces code duplication. 
 Page objects are a way to encapsulate the information about the elements and actions you can take on a page.

## BaseTest Class

This is the parent class for all test classes in the framework. It sets up the basic prerequisites that are required for all test classes, like setting up the request specification, creating endpoint instances, etc. All the test classes should inherit from this BaseTest class to ensure consistency and avoid code repetition.

## Test Classes and Endpoint Classes

The framework includes the following test and endpoint classes:

### Test Classes:

    SitesGetTest
    VppsGetTest
    VppsSitesPostDeleteTest

### Endpoint Classes:

    SitesGetEndpoint
    VppsGetEndpoint
    VppsSitesPostDelEndpoint

## Project Structure

The project follows a standard structure which segregates the page objects, test data, configurations, utilities, and test cases.

    src/test/java/endpoints: Contains page classes representing the endpoints in your API.

    src/test/java/tests: Contains the test classes that execute the tests.

    src/test/java/lib: This is where BaseTest and Assertions files are stored.

## Setup & Installation

    Install Java JDK.
    Install Maven.
    Install Allure.
    Clone the repository.
    Navigate to the project root directory and run mvn clean install to install all dependencies and build the project.

## Running Tests

To run the tests, navigate to the project root directory and execute the following command:

mvn test

## Generating Report

After tests are run, Allure can be used to generate a detailed report. Run the following command to generate the report:

allure serve /path/to/allure-results

## API Specification

| Method  | Endpoint       | Parameters/Payload    | Description                                                    |
| ------- | -------------- | --------------------- | -------------------------------------------------------------- |
| GET     | /sites         | -                     | Returns list of registered sites                               |
| GET     | /sites/(id)    | id: id of a site      | Returns details of a site                                      |
| GET     | /vpps/         | -                     | Returns list of VPPs                                          |
| GET     | /vpps/(id)     | id: id of a VPP       | Returns details of a VPP                                      |
| POST    | /vpps/(id)/sites | sample: {"site_id": 1} | Add a site to VPP. Increases the capacity of the VPP         |
| DELETE  | /vpps/(id)/sites | sample: {"site_id": 1} | Delete a site from VPP. Decreases the capacity of the VPP    |

