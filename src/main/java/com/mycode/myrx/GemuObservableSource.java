package com.mycode.myrx;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;

@Component
public class GemuObservableSource extends ObservableSource {

    public GemuObservableSource() {
        super("gemu");
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
