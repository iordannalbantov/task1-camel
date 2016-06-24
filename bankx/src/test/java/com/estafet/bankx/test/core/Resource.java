package com.estafet.bankx.test.core;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Yordan Nalbantov.
 */
public abstract class Resource {

    private static final int DEFAULT_BUFFER_SIZE = 4096;
    private static final Charset DEFAULT_ENCODING = StandardCharsets.UTF_8;
    private static final Logger logger = Logger.getLogger(Resource.class.getSimpleName());

    public static String baseURI = null;

    private Resource() {
    }

    public static <T> T json(String resourceURI, Class<T> clazz) throws IOException {
        String resource = Resource.resource(resourceURI);

        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(resource, clazz);
    }

    /**
     * This method converts JSON message, presented as String, into JSON object from the specified class.
     *
     * @param resource The String representation of JSON message.
     * @param clazz    The class to convert to.
     * @param <T>      Generic for type safety.
     * @return The result object of clazz type.
     * @throws IOException Exception on any error.
     */
    public static <T> T jsonFromString(String resource, Class<T> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(resource, clazz);
    }

    public static String resource(String resourceURI) {
        String baseEvaluatedURI = baseURI != null ? baseURI : "";
        InputStream inputStream = Resource.class.getClassLoader().getResourceAsStream(baseEvaluatedURI + resourceURI);
        return toString(inputStream);
    }

    private static String toString(final InputStream inputStream) {
        return toString(inputStream, DEFAULT_BUFFER_SIZE, DEFAULT_ENCODING);
    }

    private static String toString(final InputStream is, final int bufferSize, final Charset encoding) {
        final char[] buffer = new char[bufferSize];
        final StringBuilder result = new StringBuilder();
        try (Reader reader = new InputStreamReader(is, encoding)) {
            while (true) {
                int numberOfCharacters = reader.read(buffer, 0, buffer.length);
                if (numberOfCharacters < 0) {
                    break;
                }
                result.append(buffer, 0, numberOfCharacters);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return result.toString();
    }
}
