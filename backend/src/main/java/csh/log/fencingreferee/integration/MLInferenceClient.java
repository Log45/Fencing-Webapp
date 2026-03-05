package csh.log.fencingreferee.integration;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import csh.log.fencingreferee.api.dto.ScoreBoutResponse;

@Component
public class MlInferenceClient {

    private final RestTemplate restTemplate;

    public MlInferenceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ScoreBoutResponse scoreBout(String videoObjectKey) {

        HttpEntity<Map<String, String>> request =
            new HttpEntity<>(Map.of("video_object_key", videoObjectKey));

        ResponseEntity<ScoreBoutResponse> response =
            restTemplate.exchange(
                "http://ml-service:8000/score-bout",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<ScoreBoutResponse>() {}
            );

        System.out.println(response.toString());
        return response.getBody();
    }
}