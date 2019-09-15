package pers.ciliwfes.jstudy.jhm.generate;


/**
 * 单实例运行
 */
public interface JMHRun {
    void test();
    default void setup(){}
    default void tearDown(){}
}
