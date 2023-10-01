# Univan API Gateway

Pra desabilitar o filtro de validaçao JWT, 
acessar a classe `filter.AuthenticationFilter` e comentar o metodo filter, conforme exemplo:

```java
public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        /*
        ServerHttpRequest request = exchange.getRequest();

        if (this.isAuthorizationHeaderMissing(request)) {
            return this.onError(exchange, HttpStatus.UNAUTHORIZED, "Authorization header is missing in request");
        }

        final String token = this.getAuthHeader(request);

        if (jwtService.isInvalid(token)) {
            return this.onError(exchange, HttpStatus.UNAUTHORIZED, "Authorization header is invalid");
        }

        this.populateRequestWithHeaders(exchange, token);
        */
        return chain.filter(exchange);
}
```

### Referências

- [Validação de Token JWT - Spring Boot Gateway](https://blog.knoldus.com/spring-cloud-gateway-with-jwt/)
- [Guia do Spring Gateway](https://spring.io/guides/gs/gateway/)