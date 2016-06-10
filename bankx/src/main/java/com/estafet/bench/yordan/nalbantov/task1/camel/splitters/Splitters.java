package com.estafet.bench.yordan.nalbantov.task1.camel.splitters;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by estafet.
 */
public class Splitters {

    public List<String> splitIbansLinkedHashMapToStrings(LinkedTreeMap data) {
        List<String> ibans = new ArrayList<>();
        Object node = data.get("ibans");
        if (node instanceof List) {
            List listNode = (List) node;
            for (Object o : listNode) {
                if (o instanceof String) {
                    ibans.add((String) o);
                }
            }
        }
        return ibans;
    }
}
