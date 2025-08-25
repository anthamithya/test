@Configuration
public class RestClientConfig {

    @Bean
    public RestClient api1RestClient(OAuth2AuthorizedClientManager manager) {
        return RestClient.builder()
                .requestInterceptor(new OAuth2ClientHttpRequestInterceptor(manager, "api1"))
                .baseUrl("https://thirdparty-api1.com/api")
                .build();
    }

    @Bean
    public RestClient api2RestClient(OAuth2AuthorizedClientManager manager) {
        return RestClient.builder()
                .requestInterceptor(new OAuth2ClientHttpRequestInterceptor(manager, "api2"))
                .baseUrl("https://thirdparty-api2.com/api")
                .build();
    }
}
