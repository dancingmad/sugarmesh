# sugarmesh

## How to Build
To build a runnable version, you will need to 

1) generate the UI package. Requires Angular CLI
src/main/angular/sugarmesh
npm install
ng build

2) mvn clean package

## How to Run
The application requires a Neo4J server using default 7687 bolt port.

`
java -jar target/sugarmesh.jar --server.port=<port> --spring.data.neo4j.uri=bolt://localhost:7687 
--spring.data.neo4j.username=<username>
--spring.data.neo4j.password=<password>
`

Create a new user or log in with an existing, the password is stored with SHA256.

Point to url of spring boot beans actuator (e.g. http://localhost:8080/actuator/beans) 
and provide the domain name (e.g. com.irongrp) - the data will be generated and can be played around with in the neo4j browser.
