package com.eicky;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @author Eicky
 * @Description: 代理类,用于反射调用Activity中的方法
 * @date: 2017/3/21 15:10
 * @version: V1.0
 */
public class DynamicHandler implements InvocationHandler {
    private WeakReference<Object> mWeakReference;
    private final HashMap<String, Method> methodMap = new HashMap<>(1);

    private DynamicHandler(){}

    public DynamicHandler(Object object){
        mWeakReference = new WeakReference<Object>(object);
    }

    public void addMethod(String methodName, Method method){
        methodMap.put(methodName, method);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object target = mWeakReference.get();
        String methodName = method.getName();
        Method reflectMethod = methodMap.get(methodName);
        if (reflectMethod != null){
            //必须设置，否则反射的方法不是public修饰报错
            reflectMethod.setAccessible(true);
            Class<?>[] parameters = reflectMethod.getParameterTypes();
            if (parameters.length > 0)
                return reflectMethod.invoke(target, args);
            else
                return reflectMethod.invoke(target);
        }
        return null;
    }
}
