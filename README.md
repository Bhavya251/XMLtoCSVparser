# XML File Upload and Parsing API

This project provides a Spring Boot application with RESTful endpoints for uploading and parsing XML files. The uploaded XML file is processed to extract specific elements and return the results.

## Features

- Upload a single XML file.
- Parse the XML file to extract `AdminContractId` elements.
- Return the extracted data as a CSV string.
- Handle invalid XML files and return appropriate error messages.

## Requirements

- Java 8 or higher
- Maven
- Spring Boot

## Installation and Usage

### 1. Clone the repository:

2. Navigate to the project directory:
```bash
Copy code
cd xmlparser
```
3. Build the project using Maven:
```bash
Copy code
mvn clean install
```
4. Run the application:
```bash
Copy code
mvn spring-boot:run
```
5. Check if the API is working:
```bash
Copy code
curl -X GET http://localhost:8080/xml
```
6. Upload an XML file to be parsed:
```bash
Copy code
curl -X POST -F "file=@path/to/your/file.xml" http://localhost:8080/xml
```
Note: Only one file is allowed at a time.
