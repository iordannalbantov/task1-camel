package com.estafet.bankx.camel.pojo;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Yordan Nalbantov.
 */
public class ReportRequestPayload extends GenericPayloadWrapper<Collection<String>> {

    @Override
    @XmlElement(name = "ibans")
    public Collection<String> getData() {
        return super.getData();
    }

    @Override
    public Collection<String> newInstance() {
        return new ArrayList<>();
    }
}
