package com.markvarga21.usermanager.util;

import org.springframework.stereotype.Component;

/**
 * A class which is used to normalize names.
 */
@Component
public class NameNormalizer {
    /**
     * Normalizes the given name.
     *
     * @param name The name to normalize.
     * @return The normalized name.
     */
    public String normalizeName(final String name) {
        String[] nameParts = name.split(" ");
        String firstName = nameParts[0];
        String lastName = nameParts[1];


        return name
                .toLowerCase()
                .replaceAll("\\s+", "");
    }
}
