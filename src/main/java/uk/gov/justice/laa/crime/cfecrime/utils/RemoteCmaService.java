package uk.gov.justice.laa.crime.cfecrime.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import uk.gov.justice.laa.crime.cfecrime.api.stateless.StatelessApiRequest;
import uk.gov.justice.laa.crime.cfecrime.api.stateless.StatelessApiResponse;

@Service
@RequiredArgsConstructor
public class RemoteCmaService {

    private static final String CMA2_ENDPOINT = "/v2/assessment/means";

    @Autowired
    private WebClient webClient;

    public StatelessApiResponse callCma(StatelessApiRequest request) {
        Mono<StatelessApiRequest> api_request = Mono.just(request);
        return webClient.post()
                .uri(CMA2_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(api_request, StatelessApiRequest.class)
                .retrieve()
                .bodyToMono(StatelessApiResponse.class)
                .block();
    }
}
