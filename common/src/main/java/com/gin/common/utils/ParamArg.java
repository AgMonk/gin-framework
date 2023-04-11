package com.gin.common.utils;

import java.lang.reflect.Parameter;
import java.util.ArrayList;

/**
 * 方法的参数和参数值
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/20 12:31
 */
public record ParamArg(Parameter parameter, Object arg) {
    /**
     * 将参数和参数值组合为列表
     * @param parameters 参数
     * @param args       参数值
     * @return 参数+参数值列表
     */
    public static ArrayList<ParamArg> merge(Parameter[] parameters, Object[] args) {
        final ArrayList<ParamArg> list = new ArrayList<>();
        for (int i = 0; i < parameters.length; i++) {
            final Parameter param = parameters[i];
            final Object arg = args[i];
            list.add(new ParamArg(param, arg));
        }
        return list;
    }


}
