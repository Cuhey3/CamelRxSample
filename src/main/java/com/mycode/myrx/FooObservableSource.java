package com.mycode.myrx;

import org.springframework.stereotype.Component;

@Component
public class FooObservableSource extends ObservableSource {

    public FooObservableSource() {
        super("foo");
    }
}
