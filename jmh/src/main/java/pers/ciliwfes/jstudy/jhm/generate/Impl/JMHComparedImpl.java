package pers.ciliwfes.jstudy.jhm.generate.Impl;


import org.openjdk.jmh.annotations.*;
import pers.ciliwfes.jstudy.jhm.generate.JMHCompared;
@State(Scope.Benchmark)
public class JMHComparedImpl implements JMHCompared {
    @Param({})
    private String classType;

    private JMHCompared compared;
    @Benchmark
    public void test_1() {
        compared.test_1();
    }

    @Benchmark
    public void test_2() {
        compared.test_2();
    }


    @Setup
    public void setup() {
        if (compared == null) {init();}
        compared.setup();
    }

    @TearDown
    public void tearDown() {
        compared.tearDown();
    }

    private JMHCompared init() {
        try {
            Class<?> _class = Class.forName(classType);
            compared = (JMHCompared)_class.getDeclaredConstructor().newInstance();
            return compared;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
