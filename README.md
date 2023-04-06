URL Shortener

An Url shortening application that generates a shorter redirect

TODO :
replace hasher with library - no
sonar - done 
cucumber - pre-release
secret management - pre-release
check help.md before submit - pre-submit
tidy up readme file - pre-submit
  validation on controller
caching
  
metrics alerts etc - pre-release
logging - pre-release
check actuator for the vulnerability - pre-submit + pre-release
hide other 5XX exceptions in advice


Pre-prod
-owner info communicated through request header is not secure enough to be used on a public API. Needs to be either
replaced by an Auth provided info (OKTA etc), or update functionality can be suspended until

Post MVP :
application admin capabilities
access data, business metrics, manage app config & data integrity or trigger compensation commands
short url longer than target url


# URL Shortener

This is a URL shortener application that allows users to convert long URLs into shorter, more manageable links. The application is built using Spring Boot and uses Postgres to store the short links.

## Features

* Users can enter a long URL and generate a short link.
* The application stores the short link and the original long URL in a Postgres database.
* Users can use the short link to be redirected to the original long URL.

## Getting Started

### Prerequisites

To run this application, you will need to have the following installed:

* Java 17 or later
* Postgres

### Installation

1. Clone the repository: `git clone https://github.com/umurcanemre/urlshortener.git`
2. Navigate to the repository: `cd urlshortener`
3. Build the application: `mvn clean package`

### Usage

1. Start the application: `mvn spring-boot:run`
2. Navigate to `http://localhost:8080` in your web browser.

## API Documentation

### Create a short URL

