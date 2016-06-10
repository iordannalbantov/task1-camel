package com.estafet.bench.yordan.nalbantov.task1.camel.converters;

import com.estafet.bench.yordan.nalbantov.task1.camel.model.IbanSingleReportEntity;
import org.apache.activemq.util.ByteArrayInputStream;
import org.apache.camel.Converter;
import org.apache.camel.component.http.HttpMessage;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by estafet on 10/06/16
 */
@Converter
public class Converters {

    @Converter
    public static Collection<String> toIbanSingleReportEntity(HttpMessage httpMessage) {
        return new ArrayList<>();
    }

    @Converter
    public static InputStream toInputStream(IbanSingleReportEntity bean) {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes(StandardCharsets.UTF_8));
        return inputStream;
    }
}
