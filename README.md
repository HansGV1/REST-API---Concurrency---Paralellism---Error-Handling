# REST API - Concurrency & Parallelism

In this repository you can find the development process of a REST API for connecting various services in which the main purpose is to bring up the functionality of a web service which main responsibility is to load a corporate wesite concurrently and through parallelism, while bringing errors (if they happen) when a service is asked through an Endpoint, from time to time programmatically it will bring up errors such as 429-status response, respresenting error handling. All services are containerized through Docker Image where main service will run. 
