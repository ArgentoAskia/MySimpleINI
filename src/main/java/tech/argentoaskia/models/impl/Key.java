package tech.argentoaskia.models.impl;

import tech.argentoaskia.models.interfaces.KeyValueItem;

import java.util.Objects;

public class Key extends KeyValueItem<String> {
    public Key(String key){
        super(key);
    }

    @Override
    public String toString() {
        return "Key{" +
                "key='" + keyOrValue + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Key key1 = (Key) o;
        return Objects.equals(keyOrValue, key1.keyOrValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyOrValue);
    }
}
