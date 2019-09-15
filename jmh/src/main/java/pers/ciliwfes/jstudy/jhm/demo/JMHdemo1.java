package pers.ciliwfes.jstudy.jhm.demo;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Random;
import java.util.concurrent.TimeUnit;


@BenchmarkMode(Mode.Throughput)//运行模式
@OutputTimeUnit(TimeUnit.SECONDS)//输出时间格式
@Warmup(iterations = 2, time = 1, timeUnit = TimeUnit.SECONDS)//在time时间内(timeUnit时间格式)进行反复预热,一共预热iterations批
@Measurement(iterations = 2, time = 1)//预热后,在time时间内(timeUnit时间格式)反复执行,执行iterations批
@State(Scope.Benchmark)//Benchmark所有测试线程共享一个实例,Thread 每个测试线程分配一个实例,Group每个线程组共享一个实例
@Threads(1)//1个线程执行
public class JMHdemo1 {
    private int warmupTimes = 0;
    private int id = new Random().nextInt();
    @Param({"1", "2", "3"})
    private int data;


    @Setup
    public void init() {
        warmupTimes = 0;
        System.err.println("当前线程类: " + id);
        System.err.println("每次预热被调用");
    }

    @TearDown
    public void end() {
        System.err.println("执行次数:" + warmupTimes);
        System.err.println("方法结束时调用");
    }

    @Benchmark
    public void check() throws InterruptedException {
        warmupTimes++;
        System.out.println(data);
        Thread.sleep(100);
    }

    @Benchmark
    @Warmup(iterations = 2, time = 1, timeUnit = TimeUnit.SECONDS)//自定义预热模式
    public void selfDo() throws InterruptedException {
        warmupTimes++;

        Thread.sleep(100);
    }

    public static void main(String[] args) throws RunnerException {
        //启动配置
        Options opt = new OptionsBuilder()
                .include(JMHdemo1.class.getSimpleName())//运行的类
                .forks(1)//开启的测试进程
                .build();

        new Runner(opt).run();
    }
}
