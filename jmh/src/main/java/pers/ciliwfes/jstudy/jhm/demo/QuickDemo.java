package pers.ciliwfes.jstudy.jhm.demo;

import org.openjdk.jmh.runner.options.TimeValue;
import pers.ciliwfes.jstudy.jhm.generate.JMHCompared;
import pers.ciliwfes.jstudy.jhm.support.JMH;

import java.util.concurrent.TimeUnit;


public class QuickDemo implements JMHCompared {


    @Override
    public void test_1() {
        try {
            TimeUnit.SECONDS.sleep(1);
            System.err.println(Thread.currentThread().getId()+"执行1");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void test_2() {
        try {
            TimeUnit.SECONDS.sleep(1);
            System.out.println(Thread.currentThread().getId()+"执行2");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JMH.toBuilder(QuickDemo.class)
                .warmupTime(new TimeValue(1,TimeUnit.SECONDS))
                .warmupIterations(1)
                .measurementTime(new TimeValue(1,TimeUnit.SECONDS))
                .measurementIterations(2)
                .threads(5)
                .forks(1)
                .build();

    }
}
