# zuul-oauth2-example

This is the example of using oauth2 authorization server with separate resource server behind Zuul proxy. It supports internal client authorization workflow (`grant_type = password`) with example usages. 

## Components

1. [zuul](zuul) is Zuul Proxy (http://localhost:8080).
1. [oauth-as](oauth-as) is OAuth2 Authorization Server (http://localhost:8080/as, behind proxy).   
1. [oauth-rs](oauth-rs) is OAuth2 Resource Server (http://localhost:8080/rs, behind proxy).

## How to use

TODO
