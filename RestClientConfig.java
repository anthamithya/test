@Configuration
public class RestClientConfig {

    // Custom interceptor for OAuth2 client credentials
    static class OAuth2ClientInterceptor implements ClientHttpRequestInterceptor {

        private final OAuth2AuthorizedClientManager manager;
        private final String clientRegistrationId;

        public OAuth2ClientInterceptor(OAuth2AuthorizedClientManager manager, String clientRegistrationId) {
            this.manager = manager;
            this.clientRegistrationId = clientRegistrationId;
        }

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            // Create authorize request for the given client ID
            OAuth2AuthorizeRequest authRequest = OAuth2AuthorizeRequest
                    .withClientRegistrationId(clientRegistrationId)
                    .principal("system") // placeholder principal
                    .build();

            // Trigger authorization to obtain token
            manager.authorize(authRequest);

            // Proceed with the request
            return execution.execute(request, body);
        }
    }
    
    @Bean("api1RestClient")
public RestTemplate api1RestClient(OAuth2AuthorizedClientManager manager) {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.getInterceptors().add(new OAuth2ClientInterceptor(manager, "api1"));
    return restTemplate;
}

@Bean("api2RestClient")
public RestTemplate api2RestClient(OAuth2AuthorizedClientManager manager) {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.getInterceptors().add(new OAuth2ClientInterceptor(manager, "api2"));
    return restTemplate;
}
}
