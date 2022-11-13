# automatic-irrigation-system

As a irrigation system which helps the automatic irrigation of agricultural lands without human intervention, system has to 
be designed to fulfil the requirement of maintaining and configuring the plots of land by the irrigation time slots and the 
amount of water required for each irrigation period.
The irrigation system should have integration interface with a sensor device to direct letting the sensor to irrigate based on 
the configured time slots/amount of water

To achieve that, we have created a Spring Boot Application to built RESTful endpoints, which is a Rapid Application Development tool based on JAVA. So we have created two RESTful endpoints as:

1. For Plot management:  http://localhost:8080/api/v1/plots
2. For Time Slot management:  http://localhost:8080/api/v1/slots
3. For Sensor Management:  http://localhost:8080/api/v1/sensors/

# System configuration
Build tool: maven 

Java version: Java 11

# Database configuration

For Database, as of now we haves supported relational databases. this can be configured on application.properties as:

spring.datasource.url=jdbc:h2:mem:testdb

spring.datasource.driverClassName=org.h2.Driver

spring.datasource.username=sa

spring.datasource.password=password

spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

# Scheduler Configuration

We have a implemented a scheduler, running in a seperate thread, that is configurable.

    irrigation.scheduler.threadPoolSize:1
    irrigation.scheduler.initialDelay:60
    irrigation.scheduler.period:300 
    irrigation.scheduler.timeUnit:SECONDS
    irrigation.scheduler.retry:5

By default shceduler will run in every 5 min (configurable) and check if any plot needs to be irrigated.

Also if sensor is not available for any plot then it will retry to get get the sensor for a certain retry limit(configurable) 


# LOMBOK settings
Also we used LOMBOK (3rd party plugin) to remove boilerplate code in the POJOs. It provides rich annotations for setter/getter, constructor etc..

You need to Enable Annotation Processing on IntelliJ IDEA

    > Settings > Build, Execution, Deployment > Compiler > Annotation Processors

ref: https://www.baeldung.com/lombok-ide

# Swagger UI

For Documentation and testing the endpoints, We have used Swagger 2 for a Spring REST web service, using the Springfox implementation of the Swagger 2 specification. (for more info of swagger, goto: https://swagger.io/ )

To use Swagger UI:

    1. run the appllication
    2. Now you can test it in your browser by visiting http://localhost:8080/swagger-ui.html

Assumptions: For Last_seen and Last_fetched fields, we have used Date-Time format as it will be more convenient to integrate it to any other application which is consuming our services.


# Running the application:

step 1. clone the repository

step 2. go to to project roor folder (eg: /home/ankit/workspace/automatic-irrigation-system)

step 3. run command:  mvn clean install (after successful build, you will get final jar file)

step 4. copy the jar file at appropriate location along with application.properties file

step 5. run command: java -jar <jat_file_name>




