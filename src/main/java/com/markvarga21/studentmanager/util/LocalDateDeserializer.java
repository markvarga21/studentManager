package com.markvarga21.studentmanager.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * A custom mapper for mapping the {@code String} representation
 * of a {@code LocalDate}.
 */
@Component
public class LocalDateDeserializer implements JsonDeserializer<LocalDate> {
    /**
     * Deserializes the {@code String} into a {@code LocalDate}.
     *
     * @param json The JSON content.
     * @param typeOfT The type.
     * @param context The context.
     * @return The deserialized {@code LocalDate}.
     * @throws JsonParseException When the format is invalid.
     */
    @Override
    public LocalDate deserialize(
            final JsonElement json,
            final Type typeOfT,
            final JsonDeserializationContext context
    )
            throws JsonParseException {
        return LocalDate.parse(
                json.getAsString(),
                DateTimeFormatter
                        .ofPattern("yyyy-MM-dd").withLocale(Locale.ENGLISH)
        );
    }
}
