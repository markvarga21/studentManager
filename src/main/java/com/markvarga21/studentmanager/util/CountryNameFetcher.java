package com.markvarga21.studentmanager.util;

import org.json.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * A util class for converting a country code
 * to the actual name of the country.
 */
@Component
@RequiredArgsConstructor
@Generated
public class CountryNameFetcher {
    /**
     * Retrieves the official name of the country
     * for the given country code.
     *
     * @param countryCode The country code.
     * @return The official name of the country.
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
