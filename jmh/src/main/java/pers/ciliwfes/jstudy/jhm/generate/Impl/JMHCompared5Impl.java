package pers.ciliwfes.jstudy.jhm.generate.Impl;


import org.openjdk.jmh.annotations.*;
import pers.ciliwfes.jstudy.jhm.generate.JMHCompared5;
@State(Scope.Benchmark)
public class JMHCompared5Impl implements JMHCompared5 {
    @Param({})
    private String classType;

    private JMHCompared5 compared5;
    @Benchmark
    public void test_1() {
        compared5.test_1();
    }
    @Benchmark
    public void test_2() {
        compared5.test_2();
    }

    @Benchmark
    public void test_3() {
        compared5.test_3();
    }

    @Benchmark
    public void test_4() {
        compared5.test_4();
    }

    @Benchmark
    public void test_5() {
        compared5.test_5();
    }

    @Setup
    public void setup() {
        if (compared5 == null) {init();}
        compared5.setup();
    }

    @TearDown
    public void tearDown() {
        compared5.tearDown();
    }

    private JMHCompared5 init() {
        try {
            Class<?> _class = Class.forName(classType);
            compared5 = (JMHCompared5)_class.getDeclaredConstructor().newInstance();
            return compared5;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
