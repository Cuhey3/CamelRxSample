package com.mycode.myrx;

import org.springframework.stereotype.Component;

@Component
public class GemuObservableSource extends ObservableSource {

    public GemuObservableSource() {
        super("gemu");
    }
}
