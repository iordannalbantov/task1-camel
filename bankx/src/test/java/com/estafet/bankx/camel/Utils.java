package com.estafet.bankx.camel;

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
public abstract class Utils {

    private static final Logger logger = Logger.getLogger(Utils.class.getSimpleName());

    private static final int DEFAULT_BUFFER_SIZE = 4096;
    private static final Charset DEFAULT_ENCODING = StandardCharsets.UTF_8;

    private Utils() {
    }

    public static <T> T json(String resourceURI, Class<T> clazz) throws IOException {
        String resource = Utils.resource(resourceURI);

        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(resource, clazz);
    }

    public static String resource(String resourceURI) {
        InputStream inputStream = Utils.class.getClassLoader().getResourceAsStream(resourceURI);
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
