package com.markvarga21.studentmanager.util;

import org.json.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * A util class for converting a country code
 * to the actual name of the country.
 */
@Component
@RequiredArgsConstructor
public class CountryNameFetcher {
    /**
     * A {@code RestTemplate} instance.
     */
    private final RestTemplate restTemplate;

    /**
     * The URL of the REST API.
     */
    @Value("${rest.api.url}")
    private String restApiUrl;

    /**
     * Retrieves the name of the country for the given country code.
     *
     * @param countryCode The country code.
     * @return The name of the country.
     */
    public String getCountryNameForCode(final String countryCode) {
        String queryPart = String.format("/%s?fields=name", countryCode);
        ResponseEntity<String> responseEntity = this.restTemplate
                .getForEntity(this.restApiUrl + queryPart, String.class);
        String body = responseEntity.getBody();
        JSONObject jsonObject = new JSONObject(body);
        JSONObject nameObject = jsonObject.getJSONObject("name");
        return nameObject.getString("official");
    }
}
