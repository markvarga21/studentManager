package com.markvarga21.studentmanager.util;

import org.json.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * A util class for converting a country code
 * to the actual name of the country.
 */
@Component
@RequiredArgsConstructor
public class CountryNameFetcher {
    /**
     * The resource loader which is used
     * to load files from the resources folder.
     */
    private final ResourceLoader resourceLoader;

    /**
     * Retrieves the name of the country for the given country code.
     *
     * @param countryCode The country code.
     * @return The name of the country.
     */
    public String getCountryNameForCode(final String countryCode) {
        JSONObject jsonObj = new JSONObject(CountryNames.COUNTRIES);
        if (jsonObj.has(countryCode)) {
            return jsonObj.getString(countryCode);
        } else {
            return countryCode;
        }
    }
}
