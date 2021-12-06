package tech.argentoaskia.models.impl;

import tech.argentoaskia.models.interfaces.KeyValueItem;

public class Value extends KeyValueItem<String> {

    public Value(String keyOrValue) {
        super(keyOrValue);
    }

    @Override
    public String toString() {
        return "Value{" +
                "value='" + keyOrValue + '\'' +
                '}';
    }
}
