package survivalgame.server.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class MethodWrapper {
    private final Method mMethod;
    private final List<Class<?>> mTypes; // Used to avoid parameters copy each time
    
    public MethodWrapper(Method method) {
        mMethod = method;
        mTypes = Arrays.asList(mMethod.getParameterTypes());
    }

    public void execute(Object methodParent, List<Object> arguments) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        mMethod.invoke(methodParent, arguments);
    }
    
    public List<Class<?>> getTypes() {
        return mTypes;
    }
}
