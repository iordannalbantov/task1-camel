package com.estafet.bench.yordan.nalbantov.task1.camel.converters;

import com.estafet.bench.yordan.nalbantov.task1.camel.model.IbanSingleReportEntity;
import org.apache.activemq.util.ByteArrayInputStream;
import org.apache.camel.Converter;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by estafet on 10/06/16
 */
@Converter
public class Converters {

    @Converter
    public static InputStream toInputStream(IbanSingleReportEntity bean) {
        InputStream inputStream = new ByteArrayInputStream("test" .getBytes(StandardCharsets.UTF_8));
        return inputStream;
    }
}
