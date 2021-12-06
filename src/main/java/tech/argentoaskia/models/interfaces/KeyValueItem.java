package tech.argentoaskia.models.interfaces;

import tech.argentoaskia.models.Item;

public abstract class KeyValueItem<T> implements Item<T> {
    protected T keyOrValue;

    public KeyValueItem(T keyOrValue){
        this.keyOrValue = keyOrValue;
    }

    @Override
    public T get() {
        return keyOrValue;
    }
}
