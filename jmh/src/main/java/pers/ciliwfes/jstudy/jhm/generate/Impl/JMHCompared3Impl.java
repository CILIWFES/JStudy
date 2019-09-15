package pers.ciliwfes.jstudy.jhm.generate.Impl;


import org.openjdk.jmh.annotations.*;
import pers.ciliwfes.jstudy.jhm.generate.JMHCompared3;

@State(Scope.Benchmark)
public class JMHCompared3Impl implements JMHCompared3 {

    @Param({})
    private String classType;

    private JMHCompared3 compared3;
    @Benchmark
    public void test_1() {
        compared3.test_1();
    }
    @Benchmark
    public void test_2() {
        compared3.test_2();
    }

    @Benchmark
    public void test_3() {
        compared3.test_3();
    }

    @Setup
    public void setup() {
        if (compared3 == null) {init();}
        compared3.setup();
    }

    @TearDown
    public void tearDown() {
        compared3.tearDown();
    }

    private JMHCompared3 init() {
        try {
            Class<?> _class = Class.forName(classType);
            compared3 = (JMHCompared3)_class.getDeclaredConstructor().newInstance();
            return compared3;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
