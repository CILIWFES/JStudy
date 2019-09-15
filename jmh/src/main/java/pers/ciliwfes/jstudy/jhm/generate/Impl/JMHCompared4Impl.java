package pers.ciliwfes.jstudy.jhm.generate.Impl;


import org.openjdk.jmh.annotations.*;
import pers.ciliwfes.jstudy.jhm.generate.JMHCompared4;

@State(Scope.Benchmark)
public class JMHCompared4Impl implements JMHCompared4 {

    @Param({})
    private String classType;

    private JMHCompared4 compared4;
    @Benchmark
    public void test_1() {
        compared4.test_1();
    }
    @Benchmark
    public void test_2() {
        compared4.test_2();
    }

    @Benchmark
    public void test_3() {
        compared4.test_3();
    }

    @Benchmark
    public void test_4() {
        compared4.test_4();
    }

    @Setup
    public void setup() {
        if (compared4 == null) {init();}
        compared4.setup();
    }

    @TearDown
    public void tearDown() {
        compared4.tearDown();
    }

    private JMHCompared4 init() {
        try {
            Class<?> _class = Class.forName(classType);
            compared4 = (JMHCompared4)_class.getDeclaredConstructor().newInstance();
            return compared4;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
