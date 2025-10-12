package com.example.shangting.service;


import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class RentPredictionService {

    private final WebClient webClient = WebClient.create("http://localhost:8000");

    public double predictRent(Map<String, Object> propertyData) {
        Map<String, Object> response = webClient.post()
                .uri("/predict")
                .bodyValue(propertyData)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        return (double) response.get("predicted_rent");
    }
}
