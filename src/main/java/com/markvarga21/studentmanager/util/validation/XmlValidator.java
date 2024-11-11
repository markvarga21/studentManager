package com.markvarga21.studentmanager.util.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A utility class which is used to validate XML content.
 */
@Component
@Slf4j
public class XmlValidator {
    /**
     * The path to the XML schema.
     */
    private static final String XML_SCHEMA_PATH = "/app/schemas/students.xsd";

    /**
     * Validates the XML content.
     *
     * @param xml The XML content to be validated.
     * @return Whether the XML content is valid.
     */
    public boolean isXmlValid(final String xml) throws IOException {
        Path tempFile = Files.createTempFile("temp", ".xml");
        Files.writeString(tempFile, xml);
        SchemaFactory factory = SchemaFactory
                .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        File xmlSchema = new File(XML_SCHEMA_PATH);
        try {
            Schema schema = factory.newSchema(xmlSchema);
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(tempFile.toFile()));
        } catch (SAXException e) {
            log.error("Invalid XML content: " + e.getMessage());
            return false;
        }
        return true;
    }
}
