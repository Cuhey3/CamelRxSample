package com.mycode.myrx;

import org.springframework.stereotype.Component;

@Component
public class ZonObservableSource extends ObservableSource {

    public ZonObservableSource() {
        super("zon");
    }
}
