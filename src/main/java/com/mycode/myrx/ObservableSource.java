package com.mycode.myrx;

import java.util.ArrayList;
import java.util.List;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.rx.ReactiveCamel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rx.Observable;

@Component
public abstract class ObservableSource extends RouteBuilder {

    @Autowired
    ReactiveCamel rx;
    @Autowired
    Broker broker;
    String sourceKind;
    String observedEndpoint;
    String toObserveEndpoint;

    @Override
    public void configure() throws Exception {
        from(observedEndpoint)
                .bean(this, "compute()")
                .routingSlip(broker.getSlipUri(toObserveEndpoint));
    }

    public ObservableSource(String kind) {
        this.sourceKind = kind;
        this.observedEndpoint = "seda:observed_" + kind;
        this.toObserveEndpoint = "seda:to_observe_" + kind;
    }

    public void watch(String kindsString) {
        createObservable(kindsString.split(","));
    }

    public void observed() {
        Observable<Boolean> observable = Observable.just(true);
        rx.sendTo(observable, observedEndpoint);
    }

    public void compute() {
        System.out.println(sourceKind);
    }

    public void createObservable(String[] kinds) {
        Observable<Boolean> observable = null;
        if (kinds.length == 0) {
        } else if (kinds.length == 1) {
            observable = rx.toObservable(broker.createEndpoint("seda:to_observe_" + kinds[0])).map((Message t) -> true);
        } else {
            List<Observable<Message>> list = new ArrayList<>();
            for (String k : kinds) {
                list.add(rx.toObservable(broker.createEndpoint("seda:to_observe_" + k)));
            }
            observable = Observable.combineLatest(list, (Object... args) -> true);
        }
        if (observable != null) {
            rx.sendTo(observable, observedEndpoint);
        }
    }
}
