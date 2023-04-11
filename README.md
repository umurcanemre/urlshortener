URL Shortener

An Url shortening application that generates a shorter redirect

# URL Shortener

This is a URL shortener application that allows users to convert long URLs into shorter, more manageable links. The
application is built using Spring Boot and uses Postgres to store the short links.

## Features

* Users can enter a long URL and generate a short link.
* The application stores the short link and the original long URL in a Postgres database.
* Users can use the short link to be redirected to the original long URL.

## Getting Started

### Prerequisites

To run this application, you will need to have the following installed:

* Java 17 or later
* Postgres or docker

### Installation

1. Clone the repository: `git clone https://github.com/umurcanemre/urlshortener.git`
2. Navigate to the repository: `cd urlshortener`
3. Build the application: `./mvnw clean package`

### Usage

1. Start the application: `./mvnw spring-boot:run`
2. Import from directory `local`, postman collection exports
3. Make a Post request to shorten given or another URL of your choice
4. Make a Get request using the targetIdentifier response of earlier request to be directed to target address

## API Documentation

## Considerations

### DB selection

At the moment, DB records are considered "immutable". This makes many NoSQL DBs a good solution as they offer easier
horizontal scaling. SQLDB would also work but as the entries grow, scaling through sharding etc may not be as easy

In this implementation I've chose postgres to get some SQL DB exposure.

### Caching

This is a very suitable product for caching thanks to it's immutable and write once read many times nature

For the MVP cache is not really needed. In case of considerable traffic, DB layer caching or Rest caching can be
configured

### Deployment

Depending on the business considerations, ideally before MVP, a CI/CD pipeline would ensure fast and higher quality
delivery.

Unified format check > build > tests (unit, integration, behaviour, contract etc) > dockerize > deployment (in case of
heavy traffic canary release model can be considered to have a contained impact in case of newly introduced bugs or
other issues)

### Testing

As example tests are done to cover "shorten url" functionality. Testing was implemented on slices as Controller layer,
unit tests on application layer and containerized repository tests. In a prod environment(MVP) some sort of test to
cover entire application and it's behaviour would be needed (eg. cucumber). To ensure integration with downstream
services contract testing can be added (post-MVP). On public facing application it's important to have a tracked
performance testing too to monitor historical performance trends (latency, errors, possible hotspots etc)

### Configuration

At the moment as local-only, all configurations and secrets are in properties file. Those credentials need to be moved
to a secret manager and handled implementing the best secret handling practices (MVP). Also post-MVP continuous
configuration can be implemented to keep the application dynamically configured without needing application restarts

Spring profiles is not utilized in this application to make it easier to run locally. Explicit and clear spring profile
needs to be set MVP

### Logging and metrics

For logs a logging aggregator and distributed tracing needs to be enabled. Business, performance metrics would also need
to be tracked

### Security

To improve security in scope of MVP, credentials in properties file needs to be stored in a secure secretmanager. Before
MVP deployment, actuator vulnerability needs to be handled since actuator can expose sensitive endpoints to give
memory/thread dumps etc.

### Post-MVP

Setting up a zero-trust application to secure everything that needs to be (e.g. developers do not have write access to
prod db, ideally not even read access should be needed or granted on need (bug ticket) basis). 
