package com.mycode.myrx;

import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Headers;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.rx.ReactiveCamel;
import rx.Observable;

public class App {

    public static void main(String[] args) throws Exception {
        Broker broker = new Broker();
        CamelContext context = new DefaultCamelContext();
        context.addRoutes(new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                from("timer:foo?period=3s")
                        .setBody().constant(10)
                        .process(broker.setSlipEndpoints("foo_a")).routingSlip(header("slipEndpoints"), ",");
                from("timer:foo?period=2s")
                        .setBody().constant(7)
                        .process(broker.setSlipEndpoints("foo_b")).routingSlip(header("slipEndpoints"), ",");
                from("timer:foo?period=5s")
                        .setBody().constant(3)
                        .process(broker.setSlipEndpoints("foo_c")).routingSlip(header("slipEndpoints"), ",");
            }
        });
        ReactiveCamel rx = new ReactiveCamel(context);
        Observable<Message> obs1 = rx.toObservable(broker.createEndpoint("foo_a"));
        Observable<Message> obs2 = rx.toObservable(broker.createEndpoint("foo_b"));
        Observable<Message> obs3 = rx.toObservable(broker.createEndpoint("foo_c"));
        Observable<Message> obs4 = rx.toObservable(broker.createEndpoint("foo_a"));
        Observable.combineLatest(obs1, obs2, (Message t1, Message t2) -> {
            System.out.println(t1.getHeader("kind") + " " + t1.getBody() + " " + t2.getHeader("kind") + " " + t2.getBody());
            return t1.getBody(Integer.class) + t2.getBody(Integer.class);
        }).subscribe();
        Observable.combineLatest(obs4, obs3, (Message t1, Message t2) -> {
            System.out.println(t1.getHeader("kind") + " " + t1.getBody() + " " + t2.getHeader("kind") + " " + t2.getBody());
            return t1.getBody(Integer.class) + t2.getBody(Integer.class);
        }).subscribe();
        context.start();
        Thread.sleep(Long.MAX_VALUE);
    }

    static class Broker {

        final static Map<String, Integer> count = new LinkedHashMap<>();

        public Processor setSlipEndpoints(String kind) {
            return (Exchange exchange) -> {
                Integer get1 = count.get(kind);
                if (get1 == null) {
                    get1 = 0;
                }
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < get1; i++) {
                    if (sb.length() == 0) {
                        sb.append("seda:").append(kind).append(i);
                    } else {
                        sb.append(",seda:").append(kind).append(i);
                    }
                }
                exchange.getIn().setHeader("slipEndpoints", new String(sb));
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
            return "seda:" + kind + get;
        }
    }
}
