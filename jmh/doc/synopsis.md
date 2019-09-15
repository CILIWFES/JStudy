# 使用说明
[官方案例](http://hg.openjdk.java.net/code-tools/jmh/file/tip/jmh-samples/src/main/java/org/openjdk/jmh/samples/)

[网页1](https://www.jianshu.com/p/23abfe3251ca)

[网页2](https://www.cnblogs.com/fightfordream/p/9353002.html)

##### 在软件开发中，除要写出正确的代码之外，还需要写出高效的代码。这在并发编程中更加重要，原因主要有两点。
1. 一部分并发程序由串行程序改造而来，其目的就是提高系统性能，因此，自然需要有一种方法对两种算法进行性能比较
2. 由于业务原因引入的多线程有可能因为线程并发控制导致性能损耗，因此要评估损耗的比重是否可以接受。无论出自何种原因需要进行性能评估，量化指标总是必要的。
##### 在大部分场合，简单地回答谁快谁慢是远远不够的，如何将程序性能量化呢？这就是本节要介绍的Java微基准测试框架JMH。


# 组件介绍

## BenchmarkMode
BenchmarkMode包含测试的五种测量的纬度
1. Throughput：整体吞吐量，表示1秒内可以执行多少次调用。
2. AverageTime：调用的平均时间，指每一次调用所需要的时间。
3. SampleTime：抽样检测，在每个时间片段内抽样，最后生成直方图
4. SingleShotTime：仅仅检测一次，冷启动
5. All：所有运行模式（执行4）



## OutputTimeUnit
代表测量的单位，比如秒级别，毫秒级别，微妙级别等等。

该注解可以用在方法级别和类级别，当用在类级别的时候会被更加精确的方法级别的注解覆盖，原则就是离目标更近的注解更容易生效。


## Warmup
配置预热的内容，可用于类或者方法上，越靠近执行方法的地方越准确。一般配置warmup的参数有这些：
1. iterations：预热的次数。
2. time：每次预热的时间。
3. timeUnit：时间单位，默认是秒。
4. batchSize：批处理大小，每次操作调用几次方法。

## Measurement
配置测试的内容，可用于类或者方法上，越靠近执行方法的地方越准确。一般配置Measurement的参数有这些：
1. iterations：预热的次数。
2. time：每次预热的时间。
3. timeUnit：时间单位，默认是秒。
4. batchSize：批处理大小，每次操作调用几次方法。



## Benchmark
@Benchmark标签是用来标记测试方法的，只有被这个注解标记的话，该方法才会参与基准测试，但是有一个基本的原则就是被@Benchmark标记的方法必须是public的。


## Iteration和Invocation区别
通常看到这里我们会比较迷惑Iteration和Invocation区别，我们在配置Warmup的时候默认的时间是的1s，即1s的执行作为一个Iteration，假设每次方法的执行是100ms的话，那么1个Iteration就代表10个Invocation。

## State
在很多时候我们需要维护一些状态内容，比如在多线程的时候我们会维护一个共享的状态，这个状态值可能会在每隔线程中都一样，也有可能是每个线程都有自己的状态，JMH为我们提供了状态的支持。该注解只能用来标注在类上，因为类作为一个属性的载体。 @State的状态值主要有以下几种：

1. Scope.Benchmark：该状态的意思是会在所有的Benchmark的工作线程中共享变量内容。
2. Scope.Group：同一个Group的线程可以享有同样的变量
3. Scope.Thread：每隔线程都享有一份变量的副本，线程之间对于变量的修改不会相互影响。

它主要是方便框架来控制变量的过程逻辑，通过@State标示的类都被用作属性的容器，然后框架可以通过自己的控制来配置不同级别的隔离情况。被@Benchmark标注的方法可以有参数，但是参数必须是被@State注解的，就是为了要控制参数的隔离。

## Setup 
必须标示在@State注解的类内部，表示初始化操作（先执行）

## TearDown 
必须表示在@State注解的类内部，表示销毁操作（后执行）


## Setup和TearDown的三种纬度的控制：

1. Level.Trial 只会在个基础测试的前后执行。包括Warmup和Measurement阶段，一共只会执行一次。
2. Level.Iteration 每次执行记住测试方法的时候都会执行，如果Warmup和Measurement都配置了2次执行的话，那么@Setup和@TearDown配置的方法的执行次数就4次。
4. Level.Invocation 每个方法执行的前后执行（一般不推荐这么用）


## Param
在很多情况下，我们需要测试不同的参数的不同结果，但是测试的了逻辑又都是一样的，JMH提供了@Param参数来帮助我们处理这个事情，被@Param注解标示的参数组会一次被benchmark使用到。
##### 执行次数=n个参数各执行一次

```java
@Param({"1", "2", "3"})
int testNum;
```
## Threads
测试线程的数量，可以配置在方法或者类上，代表执行测试的线程数量。

# 案例
```java

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
public class Main {
    private int warmupTimes = 0;
    private int id = new Random().nextInt();
    @Param({"1","2","3"})
    private int  data;

    public Main() {
    }

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
                .include(Main.class.getSimpleName())//运行的类
                .forks(1)//开启的测试进程
                .build();

        new Runner(opt).run();
    }
}
```

# 组件默认配置
## org.openjdk.jmh.runner.Defaults
```java
//吞吐 
//1次-10秒          预热 
//5次-10秒-5进程    测试
public class Defaults {

    //默认运行模式(吞吐模式)
    public static final Mode BENCHMARK_MODE = Mode.Throughput;

    //预热迭代次数
    public static final int WARMUP_ITERATIONS = 5;

    //预热批大小
    public static final int WARMUP_BATCHSIZE = 1;

    //预热单次迭代时间
    public static final TimeValue WARMUP_TIME = TimeValue.seconds(10);

    //测试次数
    public static final int MEASUREMENT_ITERATIONS = 5;

    //测试批大小
    public static final int MEASUREMENT_BATCHSIZE = 1;

    //测试单次迭代运行时间
    public static final TimeValue MEASUREMENT_TIME = TimeValue.seconds(10);

    //测试线程数量
    public static final int THREADS = 1;
    
    //测试进程数
    public static final int MEASUREMENT_FORKS = 5;
    
    //默认输出时间格式
    public static final TimeUnit OUTPUT_TIMEUNIT = TimeUnit.SECONDS;
    

    //预热新建进程数(不要乱设)
    public static final int WARMUP_FORKS = 0;

    //预热时机 {@link org.openjdk.jmh.runner.options.WarmupMode}.
    public static final WarmupMode WARMUP_MODE = WarmupMode.INDI;

    //测试模式为单次运行时的启动方式 {@link org.openjdk.jmh.annotations.Mode#SingleShotTime} mode.
    public static final int WARMUP_ITERATIONS_SINGLESHOT = 0;

    //单次运行时,测试案例执行次数{@link org.openjdk.jmh.annotations.Mode#SingleShotTime} mode.
    public static final int MEASUREMENT_ITERATIONS_SINGLESHOT = 1;

    /**
     * Should JMH fail on benchmark error?
     */
    public static final boolean FAIL_ON_ERROR = false;

    /**
     * Should JMH synchronize iterations?
     */
    public static final boolean SYNC_ITERATIONS = true;

    /**
     * Should JMH do GC between iterations?
     */
    public static final boolean DO_GC = false;

    /**
     * The default {@link org.openjdk.jmh.results.format.ResultFormatType} to use.
     */
    public static final ResultFormatType RESULT_FORMAT = ResultFormatType.CSV;

    /**
     * Default prefix of the result file.
     */
    public static final String RESULT_FILE_PREFIX = "jmh-result";

    /**
     * Default {@link org.openjdk.jmh.runner.options.VerboseMode}.
     */
    public static final VerboseMode VERBOSITY = VerboseMode.NORMAL;

    /**
     * Default operations per invocation.
     */
    public static final Integer OPS_PER_INVOCATION = 1;

    /**
     * Default timeout.
     */
    public static final TimeValue TIMEOUT = TimeValue.minutes(10);

    /**
     * Default benchmarks to include.
     */
    public static final String INCLUDE_BENCHMARKS = ".*";

}

```