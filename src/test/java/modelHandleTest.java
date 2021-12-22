import org.junit.Test;
import tech.argentoaskia.models.Item;
import tech.argentoaskia.models.handle.ModelHandler;
import tech.argentoaskia.models.interfaces.DocumentItem;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class modelHandleTest {

    @Test
    public void testDynamicProxy(){
        DocumentItem documentItem = (DocumentItem) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{DocumentItem.class}, new ModelHandler() {
            @Override
            protected void init() {

            }

            @Override
            protected List<Rule> defineImplRules(Map<String, Method> currentRules, List<Rule> result) {
                return null;
            }

            @Override
            protected void beforeInvoke(Item<?> item, Method method, Object[] args, Proxy proxy) {

            }

            @Override
            protected Object afterInvoke(Object result, Collection<Item<?>> items, Set<Method> methods, Method invokeMethod, Object[] args, Proxy proxy) {
                return null;
            }
        });
        System.out.println(documentItem);
    }
}
