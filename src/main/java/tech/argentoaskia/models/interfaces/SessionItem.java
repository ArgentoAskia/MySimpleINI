package tech.argentoaskia.models.interfaces;

import tech.argentoaskia.models.Item;
import tech.argentoaskia.models.impl.Key;
import tech.argentoaskia.models.impl.Value;

import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public abstract class SessionItem<T> implements Item<T> {
    protected HashMap<Key, Value> keyValueHashMap;
    protected T sessionName;
    public SessionItem(T nameFlag){
        sessionName = nameFlag;
        keyValueHashMap = new HashMap<>();
    }

    public Set<Key> keySet(){
        return keyValueHashMap.keySet();
    }
    public Value getValue(Key key){
        return keyValueHashMap.get(key);
    }
    public Value putKeyValue(Key key, Value value){
        return keyValueHashMap.put(key, value);
    }
    public int keyCount(){
        return keyValueHashMap.size();
    }

    @Override
    public T get() {
        return sessionName;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (o.getClass() == getClass()){
            SessionItem<?> that = (SessionItem<?>) o;
            return Objects.equals(sessionName, that.sessionName);
        }else if (o.getClass() == sessionName.getClass()){
            T that = (T) o;
            return Objects.equals(sessionName, that);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionName);
    }
}
