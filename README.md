Here is a `README.md` file for your project:

```markdown
# TC Automation

## Overview

This project is a Spring Boot application that uses Apache Camel for routing and processing tasks. It includes functionalities for interacting with databases and executing commands on remote servers.

## Features

- Delete and insert data into the `AIT_SCAN_WINDOW` table.
- Select data from the `AIT_SCAN_WINDOW` table and compare it against a given `AIT_NUMBER`.
- Delete data from the `KAFKA_STAT` table based on a given condition.
- Run a Java process in the background on a remote Linux server.
- Query the `KAFKA_STAT` table.

## Technologies Used

- Java
- Spring Boot
- Apache Camel
- JPA (Jakarta Persistence API)
- Maven
- JSch (Java Secure Channel)

## Prerequisites

- JDK 21 or higher
- Maven 3.6.0 or higher
- A running instance of a database (e.g., MySQL, PostgreSQL)
- SSH access to a remote Linux server

## Setup

1. **Clone the repository:**
   ```sh
   git clone https://github.com/GowthamOnCoding/tc-automation.git
   cd tc-automation
   ```

2. **Configure the application properties:**
   Update the `src/main/resources/application.properties` file with your database and SSH configurations.

3. **Build the project:**
   ```sh
   mvn clean install
   ```

4. **Run the application:**
   ```sh
   mvn spring-boot:run
   ```

## Usage

### Endpoints

- **Delete and Insert AIT_SCAN_WINDOW:**
   - **Endpoint:** `direct:deleteAndInsertAitScanWindow`
   - **Headers:** `aitNo`
   - **Body:** `Map<String, Object>` containing the row data

- **Select and Compare AIT_SCAN_WINDOW:**
   - **Endpoint:** `direct:selectAndCompareAitScanWindow`
   - **Headers:** `aitNo`

- **Delete KAFKA_STAT:**
   - **Endpoint:** `direct:deleteKafkaStat`
   - **Headers:** `condition`

- **Run Java Process on Remote Server:**
   - **Endpoint:** `direct:runJavaProcess`

- **Query KAFKA_STAT:**
   - **Endpoint:** `direct:queryKafkaStat`

### Utility Classes

- **DbUtil:** Utility class for database operations.
- **SshUtil:** Utility class for executing commands on remote servers.

## Models

- **KafkaStat:** Represents the `kafka_stat` table.
- **AitDbProp:** Represents the `ait_dbprop` table.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
```

This `README.md` file provides an overview of the project, setup instructions, usage details, and information about the technologies used.
