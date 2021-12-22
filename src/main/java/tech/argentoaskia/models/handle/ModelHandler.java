package tech.argentoaskia.models.handle;

import tech.argentoaskia.models.Item;
import tech.argentoaskia.models.interfaces.DocumentItem;
import tech.argentoaskia.models.interfaces.KeyItem;
import tech.argentoaskia.models.interfaces.SessionItem;
import tech.argentoaskia.models.interfaces.ValueItem;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.PublicKey;
import java.util.*;

public abstract class ModelHandler implements InvocationHandler {
    // todo Init Impls here
    protected static final DocumentItem<?> documentItemImpl = null;
    protected static final SessionItem<?> sessionItemImpl = null;
    protected static final ValueItem<?> valueItemImpl = null;
    protected static final KeyItem<?> keyItemImpl = null;

    private static final HashMap<Method, Item<?>> modelMethodImpl;

    static {
        modelMethodImpl = new HashMap<>();
        for (Method m :
                DocumentItem.class.getMethods()) {
            modelMethodImpl.put(m, documentItemImpl);
        }
        for (Method m :
                SessionItem.class.getMethods()) {
            modelMethodImpl.put(m, sessionItemImpl);
        }
        for (Method m :
                ValueItem.class.getMethods()) {
            modelMethodImpl.put(m, valueItemImpl);
        }
        for (Method m :
                KeyItem.class.getMethods()) {
            modelMethodImpl.put(m, keyItemImpl);
        }
    }
    protected static class Rule{
        private final Method method;
        private final Item<?> impl;
        private Rule(Method method, Item<?> s){
            this.method = method;
            this.impl = s;
        }
        private Method getMethod(){
            return method;
        }
        private Item<?> getImpl(){
            return impl;
        }
    }
    protected final Rule createRule(Method method, Item<?> impl){
        Objects.requireNonNull(method);
        Objects.requireNonNull(impl);
        return new Rule(method,impl);
    }

    // init
    public ModelHandler(){
        init();
        addMethodImplRules();
    }
    private void addMethodImplRules(){
        HashMap<String, Method> currentRules = new HashMap<>();
        Set<Method> methods = modelMethodImpl.keySet();
        for (Method m :
                methods) {
            String methodName = m.getName();
            currentRules.put(methodName, m);
        }
        List<Rule> newRules = defineImplRules(currentRules, new LinkedList<>());
        if(newRules == null || newRules.size() == 0){
            return;
        }
        for (Rule r :
                newRules) {
            modelMethodImpl.put(r.getMethod(), r.getImpl());
        }
    }
    protected abstract void init();
    protected abstract List<Rule> defineImplRules(Map<String, Method> currentRules, List<Rule> result);



    /**
     * Processes a method invocation on a proxy instance and returns
     * the result.  This method will be invoked on an invocation handler
     * when a method is invoked on a proxy instance that it is
     * associated with.
     *
     * @param proxy  the proxy instance that the method was invoked on
     * @param method the {@code Method} instance corresponding to
     *               the interface method invoked on the proxy instance.  The declaring
     *               class of the {@code Method} object will be the interface that
     *               the method was declared in, which may be a superinterface of the
     *               proxy interface that the proxy class inherits the method through.
     * @param args   an array of objects containing the values of the
     *               arguments passed in the method invocation on the proxy instance,
     *               or {@code null} if interface method takes no arguments.
     *               Arguments of primitive types are wrapped in instances of the
     *               appropriate primitive wrapper class, such as
     *               {@code java.lang.Integer} or {@code java.lang.Boolean}.
     * @return the value to return from the method invocation on the
     * proxy instance.  If the declared return type of the interface
     * method is a primitive type, then the value returned by
     * this method must be an instance of the corresponding primitive
     * wrapper class; otherwise, it must be a type assignable to the
     * declared return type.  If the value returned by this method is
     * {@code null} and the interface method's return type is
     * primitive, then a {@code NullPointerException} will be
     * thrown by the method invocation on the proxy instance.  If the
     * value returned by this method is otherwise not compatible with
     * the interface method's declared return type as described above,
     * a {@code ClassCastException} will be thrown by the method
     * invocation on the proxy instance.
     * @throws Throwable the exception to throw from the method
     *                   invocation on the proxy instance.  The exception's type must be
     *                   assignable either to any of the exception types declared in the
     *                   {@code throws} clause of the interface method or to the
     *                   unchecked exception types {@code java.lang.RuntimeException}
     *                   or {@code java.lang.Error}.  If a checked exception is
     *                   thrown by this method that is not assignable to any of the
     *                   exception types declared in the {@code throws} clause of
     *                   the interface method, then an
     *                   {@link UndeclaredThrowableException} containing the
     *                   exception that was thrown by this method will be thrown by the
     *                   method invocation on the proxy instance.
     * @see UndeclaredThrowableException
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Item<?> item = modelMethodImpl.get(method);
        beforeInvoke(item, method, args, (Proxy) proxy);
        Object result = method.invoke(item, args);
        return afterInvoke(result, modelMethodImpl.values(), modelMethodImpl.keySet(), method, args, (Proxy) proxy);
    }
    protected abstract void beforeInvoke(Item<?> item, Method method, Object[] args, Proxy proxy);
    protected abstract Object afterInvoke(Object result, Collection<Item<?>> items, Set<Method> methods, Method invokeMethod, Object[] args, Proxy proxy);
}
