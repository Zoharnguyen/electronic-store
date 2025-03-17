package com.altech.electronic_store.performance;

import com.altech.electronic_store.ElectronicStoreApplication;
import com.altech.electronic_store.service.BasketService;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class BasketOperationsPerformanceTest {

    private ConfigurableApplicationContext context;
    private BasketService basketService;

    @Setup
    public void setup() {
        context = SpringApplication.run(ElectronicStoreApplication.class);
        basketService = context.getBean(BasketService.class);
    }

    @TearDown
    public void tearDown() {
        context.close();
    }

    @Benchmark
    public void measureBasketCreation() {
        basketService.createBasket();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BasketOperationsPerformanceTest.class.getSimpleName())
                .forks(1)
                .warmupIterations(2)
                .measurementIterations(5)
                .build();

        new Runner(opt).run();
    }
} 