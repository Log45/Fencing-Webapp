package csh.log.fencingreferee.integration;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
// import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import csh.log.fencingreferee.api.dto.ScoreBoutResponse;

@Component
public class MlInferenceClient {

    private final RestTemplate restTemplate;
    private final String mlServiceUrl;

    public MlInferenceClient(
        RestTemplate restTemplate,
        @Value("${ML_SERVICE_URL}") String mlServiceUrl
    ) {
        this.restTemplate = restTemplate;
        this.mlServiceUrl = mlServiceUrl;
    }

    public ScoreBoutResponse scoreBout(String videoObjectKey) {

        HttpEntity<Map<String, String>> request =
            new HttpEntity<>(Map.of("video_object_key", videoObjectKey));

        ResponseEntity<ScoreBoutResponse> response =
            restTemplate.exchange(
                mlServiceUrl + "/score-bout",
                HttpMethod.POST,
                request,
                ScoreBoutResponse.class
            );

        return response.getBody();
    }
}