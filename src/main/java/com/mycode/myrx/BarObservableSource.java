package com.mycode.myrx;

import org.springframework.stereotype.Component;

@Component
public class BarObservableSource extends ObservableSource {

    public BarObservableSource() {
        super("bar");
    }
}
