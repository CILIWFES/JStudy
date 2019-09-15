package pers.ciliwfes.jstudy.jhm.support;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.ChainedOptionsBuilder;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import pers.ciliwfes.jstudy.jhm.generate.*;
import pers.ciliwfes.jstudy.jhm.generate.Impl.*;
import pers.ciliwfes.zstudy.jhm.generate.*;
import pers.ciliwfes.zstudy.jhm.generate.Impl.*;


/**
 * 组件默认在org.openjdk.jmh.runner.Defaults中
 * @see org.openjdk.jmh.runner.Defaults
 */
public class JMH extends OptionsBuilder{
    private boolean isSet = true;

    /**
     * 获取链式构造器,build时执行JMH测试
     * @param _class 测试方法,需要实现JmhGenerate 下5个接口其一
     * @return
     * @see pers.ciliwfes.zstudy.jhm.generate
     */
    public static<T> ChainedOptionsBuilder toBuilder(Class _class) {
        JMH jmh = new JMH();
        ChainedOptionsBuilder classType = jmh.include(jmh.choice(_class)).param("classType", _class.getCanonicalName());
        jmh.isSet = false;
        return classType;
    }

    /**
     * 选择具体实现类
     * @param _class
     * @return
     */
    protected String choice(Class _class) {
        if(JMHRun.class.isAssignableFrom(_class)){
            return JMHRunImpl.class.getSimpleName();
        } else if (JMHCompared.class.isAssignableFrom(_class)) {
            return JMHComparedImpl.class.getSimpleName();
        } else if (JMHCompared3.class.isAssignableFrom(_class)) {
            return JMHCompared3Impl.class.getSimpleName();
        } else if (JMHCompared4.class.isAssignableFrom(_class)) {
            return JMHCompared4Impl.class.getSimpleName();
        } else if (JMHCompared5.class.isAssignableFrom(_class)) {
            return JMHCompared5Impl.class.getSimpleName();
        }else{
            throw new IllegalArgumentException("没有找到选择路径");
        }
    }
    /**
     * 覆盖方法
     * @param regexp
     * @return
     */
    public ChainedOptionsBuilder include(String regexp) {
        if (isSet) {
            return super.include(regexp);
        }else{
            throw new IllegalArgumentException("冲突!无需初始化");
        }
    }

    /**
     * 覆盖方法
     * @param name
     * @param values
     * @return
     */
    public ChainedOptionsBuilder param(String name, String... values) {
        if("classType".equals(name)&&!isSet) throw new IllegalArgumentException("冲突!参数名不可定义为classType");
        return super.param(name, values);
    }


    /**
     * 开始基准测试
     * @return
     */
    public Options build() {
        try {
            new Runner(this).run();
        } catch (RunnerException e) {
            System.err.println("执行过程出现异常!" + e.getMessage());
            e.printStackTrace();
        }
        return this;
    }


    private JMH() {super();}

}
