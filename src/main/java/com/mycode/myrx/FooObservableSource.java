package com.mycode.myrx;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;

@Component
public class FooObservableSource extends ObservableSource {

    public FooObservableSource() {
        super("foo");
    }

    @Override
    public void compute() {
        super.compute();
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(DamuObservableSource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
