package com.mycode.myrx;

import org.apache.camel.CamelContext;
import org.apache.camel.rx.ReactiveCamel;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBootApp implements CommandLineRunner {

    @Autowired
    CamelContext context;
    @Autowired
    BeanFactory factory;

    public static void main(String[] args) throws InterruptedException {
        new SpringApplication(SpringBootApp.class).run();
        Thread.sleep(Long.MAX_VALUE);
    }

    @Bean
    public ReactiveCamel getReactiveCamel() {
        return new ReactiveCamel(context);
    }

    @Override
    public void run(String... strings) throws Exception {
        FooObservableSource foo = factory.getBean(FooObservableSource.class);
        BarObservableSource bar = factory.getBean(BarObservableSource.class);
        GemuObservableSource gemu = factory.getBean(GemuObservableSource.class);
        foo.watch("foo");
        bar.watch("gemu");
        gemu.watch("foo");
        gemu.start();
    }
}
