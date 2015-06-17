package com.mycode.myrx;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.springframework.stereotype.Component;

@Component
public class Broker {

    private final Map<String, Integer> count = new LinkedHashMap<>();

    public Expression getSlipUri(final String kind) {
        return new Expression() {

            @Override
            public <T> T evaluate(Exchange arg0, Class<T> arg1) {
                Integer c = count.get(kind);
                if (c == null) {
                    c = 0;
                }
                ArrayList<String> list = new ArrayList<>();
                for (int i = 0; i < c; i++) {
                    list.add(kind + i);
                }
                return (T) list;
            }
        };
    }

    public String createEndpoint(String kind) {
        Integer get = count.get(kind);
        if (get == null) {
            count.put(kind, 1);
            get = 0;
        } else {
            count.put(kind, get + 1);
        }
        return kind + get;
    }
}
