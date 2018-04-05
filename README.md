# zuul-oauth2-example

This is the example of using oauth2 authorization server with separate resource server behind Zuul proxy. It supports internal client authorization workflow (`grant_type = password`) as well as external client authorization workflow (`grant_type = access_token`) with example usages. 

## Components

1. [zuul](zuul) is Zuul Proxy (http://localhost:8080).
1. [oauth-as](oauth-as) is OAuth2 Authorization Server (http://localhost:8080/as, behind proxy).   
1. [oauth-rs](oauth-rs) is OAuth2 Resource Server (http://localhost:8080/rs, behind proxy).
1. [internal](internal) is simple UI application (http://localhost:8080, behind proxy).
1. [external](external) is simple external UI application (http://localhost:8081). 

## How to use

1. Start all services and navigate to internal application stack proxy http://localhost:8080 to check the internal client oauth2 workflow.
1. Check also external application at http://localhost:8081 to test the external client oauth2 workflow.
