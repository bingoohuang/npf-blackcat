package com.ailk.ecs.esf.base.utils;

import java.io.Serializable;



/**
 * 三元组类型。
 *
 * @param <A> 第一值类型
 * @param <B> 第二值类型
 * @param <C> 第三只类型
 */
public class Tuple<A, B, C> implements Serializable {
    private static final long serialVersionUID = -6903466261001542594L;
    private A first;
    private B second;
    private C third;

    /**
     * ctor。
     * @param first 第一值
     * @param second 第二值
     * @param third 第三值
     */
    public Tuple(A first, B second, C third) {
        this.setFirst(first);
        this.setSecond(second);
        this.setThird(third);
    }

    /**
     * 设置第一值。
     * @param first 第一值
     */
    public void setFirst(A first) {
        this.first = first;
    }

    /**
     * 取得第一值。
     * @return 第一值
     */
    public A getFirst() {
        return first;
    }

    /**
     * 设置第二值。
     * @param second 第二值
     */
    public void setSecond(B second) {
        this.second = second;
    }

    /**
     * 取得第二值。
     * @return 第二值
     */
    public B getSecond() {
        return second;
    }

    /**
     * 设置第三值。
     * @param third 第三值
     */
    public void setThird(C third) {
        this.third = third;
    }

    /**
     * 取得第三值。
     * @return 第三值
     */
    public C getThird() {
        return third;
    }


    @Override
    public String toString() {
        return "(" + first + ", " + second + ", " + third + ")";
    }

    /**
     * 构造三元组。
     * @param <A> 第一值类型
     * @param <B> 第二值类型
     * @param <C> 第三值类型
     * @param a 第一值
     * @param b 第二值
     * @param c 第三值
     * @return 三元值对象
     */
    public static <A, B, C> Tuple<A, B, C> makeTuple(A a, B b, C c) {
        return new Tuple<A, B, C>(a, b, c);
    }
}
