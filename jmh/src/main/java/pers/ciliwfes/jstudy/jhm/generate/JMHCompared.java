package pers.ciliwfes.jstudy.jhm.generate;


public interface JMHCompared {
    void test_1();
    void test_2();
    default void setup(){}
    default void tearDown(){}
}
