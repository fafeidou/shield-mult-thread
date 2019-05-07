package com.shield.utils;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * COPYRIGHT © 2005-2018 CHARLESKEITH ALL RIGHTS RESERVED.
 *
 * @author Batman.qin
 * @create 2019-02-25 18:36
 */
public class CatchUtil {

    //------5
    public static <T,R> R tryDo(T t, Function<T,R> func) {
        try {
            return func.apply(t);
        } catch (Exception e) {
            e.printStackTrace();  // for log
            throw new RuntimeException(e.getCause());
        }
    }

    public static <T> void tryDo(T t, Consumer<T> func) {
        try {
            func.accept(t);
        } catch (Exception e) {
            e.printStackTrace();  // for log
            throw new RuntimeException(e.getCause());
        }
    }
}
