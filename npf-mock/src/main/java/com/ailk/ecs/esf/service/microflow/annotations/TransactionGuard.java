package com.ailk.ecs.esf.service.microflow.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 事务保护标注.<br>
 * Step继承类非私有方法使用.<br>
 * 当Step继承类非私有方法使用此标注时，其内部的Dao的事务控制可以交由框架控制.<br>
 * 1）没有添加@TransactionGuard，而应用也没有显示定义事务开始结束，那么就是本地事务，
 * 每一次update/delete操作都会当做一个本地事务，自动开始结束。
 * 此时，如果有两个更新，第一个可能成功提交，第二个失败，造成数据不一致。
 * <br/>
 * 2) 如果添加了@TransactionGuard，会在update/delete前自动开始一个jdbc事务，
 * 然后在step运行结束时，统一提交，如果出现异常，则统一回滚。
 * 对于都是oracle数据源，基本上也可以保证数据一致性。 
 * 除了一种情况，就是数据源1提交成功了，但是数据源2确提交失败，此时没有XA，就会造成两个库的数据在业务上不一致
 * <br/>
 * 3) 如果添加了@TransactionGuard(xa)，则会开始一个XA的全局事务(此时要求数据源配置成XA的驱动),
 * 
 * @author Bingoo Huang
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TransactionGuard {
    /**
     * 是否使用XA事务。
     * @return
     */
    boolean xa() default false;
}
