package csh.log.fencingreferee.integration;

import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import csh.log.fencingreferee.api.dto.ScoringEventRequest;

@Component
public class MlInferenceClient {

    private final RestTemplate restTemplate;

    public MlInferenceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<ScoringEventRequest> scoreBout(String videoUrl) {

        HttpEntity<Map<String, String>> request =
            new HttpEntity<>(Map.of("video_url", videoUrl));

        ResponseEntity<List<ScoringEventRequest>> response =
            restTemplate.exchange(
                "http://ml-service:8000/score-bout",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<List<ScoringEventRequest>>() {}
            );

        return response.getBody();
    }
}