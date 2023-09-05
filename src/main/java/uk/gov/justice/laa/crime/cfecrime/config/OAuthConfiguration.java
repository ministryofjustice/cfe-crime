package uk.gov.justice.laa.crime.cfecrime.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OAuthConfiguration {

    private static final String DEV_HOST_NAME = "laa-crime-means-assessment-dev.apps.live.cloud-platform.service.justice.gov.uk";
    private static final String API_BASE_PATH = "/api/internal";
    @Bean
    WebClient webClient(ClientRegistrationRepository clientRegistrations,
                        OAuth2AuthorizedClientRepository authorizedClients) {
        var oauth =
                new ServletOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrations, authorizedClients);
        // explicitly opt into using the oauth2Login to provide an access token implicitly
        oauth.setDefaultOAuth2AuthorizedClient(true);
        oauth.setDefaultClientRegistrationId("cma-api");

        return WebClient.builder()
                .filter(oauth)
                .baseUrl("https://" + DEV_HOST_NAME + API_BASE_PATH)
                .build();
    }
}
