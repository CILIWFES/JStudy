package pers.ciliwfes.jstudy.jhm.generate.Impl;


import org.openjdk.jmh.annotations.*;
import pers.ciliwfes.jstudy.jhm.generate.JMHRun;

/**
 * 单方法测试
 */
@State(Scope.Benchmark)
public class JMHRunImpl implements JMHRun {
    private JMHRun runer;
    @Param({})
    private String classType;

    @Benchmark
    public void test() {
        runer.test();
    }

    @Setup
    public void setup() {
        if (runer == null) {init();}
        runer.setup();
    }

    @TearDown
    public void tearDown() {
        runer.tearDown();
    }

    private JMHRun init() {
        try {
            Class<?> _class = Class.forName(classType);
            runer = (JMHRun)_class.getDeclaredConstructor().newInstance();
            return runer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
