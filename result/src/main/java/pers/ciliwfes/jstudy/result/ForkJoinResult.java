package pers.ciliwfes.jstudy.result;

import pers.ciliwfes.jstudy.jhm.generate.JMHCompared4;
import pers.ciliwfes.jstudy.jhm.support.JMH;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 使用ForkJoint框架进行集合求和
 */
class ForkJoinDemo extends RecursiveTask<Long> {
    private final static Integer separator = 10000;
    private List<Integer> lst;

    public ForkJoinDemo(List<Integer> lst) {
        this.lst = lst;
    }

    @Override
    protected Long compute() {
        int size = (lst.size() / separator) + (lst.size() % separator > 0 ? 1 : 0);
        long sum = 0;
        //拆分
        if (size > 1) sum+=splitJob();
        //执行
        else sum= execute();
        return sum;
    }

    private Long splitJob() {
        int size = (lst.size() / separator) + (lst.size() % separator > 0 ? 1 : 0);
        ArrayList<ForkJoinDemo> runs = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            ForkJoinDemo runner = new ForkJoinDemo(lst.subList(i * separator, Math.min(i * separator + separator, lst.size())));
            runner.fork();
            runs.add(runner);
        }
        long sum = 0;
        for (ForkJoinDemo run : runs) {
            try {
                sum += run.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return sum;
    }

    private Long execute() {
        long sum = 0;
        for (Integer integer : lst) {
            sum += integer;
        }
        return sum;
    }
}

/**
 * ForkJoin 性能测试代码
 * CPU密集型--集合求和
 */
public class ForkJoinResult implements JMHCompared4 {
    private final int size=10000*10;
    private static List<Integer> collect;

    public ForkJoinResult() {
        collect = Stream.iterate(0, x -> x + 1).limit(size).collect(Collectors.toList());
    }

    @Override
    public void test_1() {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinDemo runner = new ForkJoinDemo(collect);
        ForkJoinTask submit = forkJoinPool.submit(runner);
        try {
            submit.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void test_2() {
        int sum = collect.parallelStream().mapToInt(x -> x).sum();
    }

    @Override
    public void test_3() {
        int sum = collect.stream().mapToInt(x -> x).sum();
    }

    @Override
    public void test_4() {
        int sum = 0;
        for (Integer integer : collect) {
            sum += integer;
        }
    }

    public static void main(String[] args) {
        JMH.toBuilder(ForkJoinResult.class).measurementIterations(2).warmupIterations(1).forks(1).build();
    }
}
