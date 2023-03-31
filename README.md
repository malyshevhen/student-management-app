# Student Management Application

This is a web application that allows for the simple management of student groups and courses. The application features REST endpoints, a one-page view, and the ability to perform basic CRUD operations within the user interface.

## Features

* View and manage student groups and courses
* Add, edit, and delete student and course information
* REST endpoints for programmatic access to the application data
* One-page view for ease of use
* Dockerfile and docker-compose.yml for easy deployment

## Modules

This application was built using the following modules:

1. Spring Boot
2. Spring Boot Starter Web
3. Spring Boot Starter Thymeleaf
4. Flyway Core
5. Testcontainers
6. JUnit Jupiter
7. PostgreSQL
8. Project Lombok
9. SLF4J
10. Mockito
11. ModelMapper
12. OpenCSV
13. Spring Boot Starter Data JPA
14. JavaScript Ajax

## Build Tool

This application was built using Maven.

## Deployment

To deploy the application, follow these steps:

1. Clone the repository to your local machine.
2. Navigate to the project directory.
3. Use the following command to launch the application with the database on any machine with Docker installed: 'docker compose up'

This will start the application and the database container. The application will be available at `http://localhost:8080`.

**Note:** The Docker image for the application is already pushed to Docker Hub, so you don't need to build the image before running `docker-compose up`.

## Usage

To use the application, navigate to the web interface or use the REST endpoints to access and manipulate data programmatically.

## Contributing

Please refer to the CONTRIBUTING.md file for guidelines on contributing to this project.

## License

This project is licensed under the MIT License. See the LICENSE.md file for details.

## Acknowledgements

This application uses third-party libraries and resources, including but not limited to:

* [Thymeleaf](https://www.thymeleaf.org/)
* [Mockito](https://site.mockito.org/)
* [OpenCSV](http://opencsv.sourceforge.net/)
* [Testcontainers](https://www.testcontainers.org/)
