package com.eicky;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author Eicky
 * @Description: 注解主类
 * @date: 2017/3/21 14:29
 * @version: V1.0
 */
public class InjectUtils {
    private static final String CONTENT_VIEW_METHOD = "setContentView";
    private static final String FIND_VIEW_METHOD = "findViewById";
    private static final String EVENT_INJECT_METHOD = "value";

    public static void inject(Activity activity){
        injectContentView(activity);
        injectViews(activity);
        injectEvents(activity);
    }
    //注入ContentView
    private static void injectContentView(Activity activity){
        Class<? extends Activity> clazz = activity.getClass();
        ContentView contentView = clazz.getAnnotation(ContentView.class);
        if (contentView != null){
            try {
                Method method = clazz.getMethod(CONTENT_VIEW_METHOD, int.class);
                method.invoke(activity, contentView.value());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
    //注入View
    private static void injectViews(Activity activity){
        Class<? extends Activity> clazz = activity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields){
            ViewInject viewInject = field.getAnnotation(ViewInject.class);
            if (viewInject != null){
                try {
                    Method method = clazz.getMethod(FIND_VIEW_METHOD, int.class);
                    Object target = method.invoke(activity, viewInject.value());
                    field.setAccessible(true);
                    field.set(activity, target);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //注册事件
    private static void injectEvents(Activity activity){
        Class<? extends Activity> clazz = activity.getClass();
        //获取所有方法
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods){
            //获取方法上的所有注解
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations){
                Class<? extends Annotation> annotationType = annotation.annotationType();
                //获取事件注解
                EventBase eventBase = annotationType.getAnnotation(EventBase.class);
                if (eventBase != null){
                    Class<?> type = eventBase.type();
                    String setter = eventBase.setter();
                    if (TextUtils.isEmpty(setter))
                        setter = "set" + type.getSimpleName();
                    String methodName = eventBase.method();
                    try {
                        Method method1 = annotationType.getMethod(EVENT_INJECT_METHOD);
                        method1.setAccessible(true);
                        int[] viewIds = (int[]) method1.invoke(annotation);
                        //通过InvocationHandler设置代理，用于反射调用activity中定义的方法
                        DynamicHandler handler = new DynamicHandler(activity);
                        handler.addMethod(methodName, method);
                        Object listener = Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, handler);
                        for (int id : viewIds){
                            View targetView = activity.findViewById(id);
                            Method setListenerMethod = targetView.getClass().getMethod(setter, type);
                            setListenerMethod.invoke(targetView, listener);
                        }
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
