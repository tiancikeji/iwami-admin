package com.iwami.iwami.app.common.dispatch;

import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@SuppressWarnings("unchecked")
public class AjaxDispatcher implements ApplicationContextAware {

    private static Logger logger = Logger.getLogger(AjaxDispatcher.class);

    public static final String KEY_PARAM = "params";
    public static final String KEY_PATH = "path";

    private static Map<String, Method> ajaxName2Method = new ConcurrentHashMap<String, Method>();
    private static Map<String, Object> ajaxName2Bean = new ConcurrentHashMap<String, Object>();

    public static String dispatch(Map<String, Object> req) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String path = (String)req.get(KEY_PATH);
        Object ins = ajaxName2Bean.get(path);
        Method m = ajaxName2Method.get(path);
        if (m == null) {
            throw new MethodNotFoundException("No matching method found for path: " + path);
        }
        Class<?>[] paramClasses = m.getParameterTypes();
        Object[] paramValues = new Object[1];

        try {
            paramValues[0] = req.get(KEY_PARAM);
        } catch (Exception e) {
            logger.error("Can not deserialize json param " + req.get(KEY_PARAM) + " to class " + paramClasses[0], e);
            throw e;
        }

        Object result = m.invoke(ins, paramValues);
        if (result instanceof String) {
            return (String) result;
        } else {
            StringWriter buf = new StringWriter();
            mapper.writeValue(buf, result);
            return buf.toString();
        }

    }

    @SuppressWarnings("rawtypes")
    public static void registerAjaxClass(Class beanClass, Object bean) {
        String prefix = ((AjaxClass) beanClass.getAnnotation(AjaxClass.class)).prefix();
        if (prefix == null) {
            prefix = "";
        }
        Method[] ms = beanClass.getMethods();
        for (int j = 0; j < ms.length; j++) {
            Method m = ms[j];
            AjaxMethod ajaxMethod = m.getAnnotation(AjaxMethod.class);
            if (ajaxMethod != null) {
                String path = ajaxMethod.path();
                if (path == null || path.trim().equals("")) {
                    path = m.getName();
                }
                String fullPath = prefix + path;
                ajaxName2Method.put(fullPath, ms[j]);
                logger.info("Registering ajax method at path: " + fullPath);
                ajaxName2Bean.put(fullPath, bean);
            }
        }
    }

    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        Map<String, Object> map = ctx.getBeansWithAnnotation(AjaxClass.class);
        Collection<Object> beans = map.values();
        for (Object bean : beans) {
            logger.info("Inspecting AjaxClass: " + bean.getClass());
            registerAjaxClass(bean.getClass(), bean);
        }
    }

    public static Map<String, Method> getDispatchMap() {
        return Collections.unmodifiableMap(ajaxName2Method);
    }

}
