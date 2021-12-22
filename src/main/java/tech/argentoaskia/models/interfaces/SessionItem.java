package tech.argentoaskia.models.interfaces;

import tech.argentoaskia.models.Item;

public interface SessionItem<S> extends Item<S> {

    int count();
    <K> KeyItem<K> getKey(int index, Class<K> typeForKeyData);
    <K> KeyItem<K> getKey(Object keyString, Class<K> typeForKeyData);
    KeyItem<?>[] getAllKeys();


}
