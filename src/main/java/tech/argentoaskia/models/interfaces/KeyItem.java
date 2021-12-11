package tech.argentoaskia.models.interfaces;

import tech.argentoaskia.models.Item;

public interface KeyItem<T> extends Item<T> {

    @Override
    Object output(Object... params);
}
