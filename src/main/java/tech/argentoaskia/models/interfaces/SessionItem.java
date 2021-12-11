package tech.argentoaskia.models.interfaces;

import tech.argentoaskia.models.Item;

import java.util.HashMap;
import java.util.Map;

public interface SessionItem<T> extends Item<T> {

    int getKeyValueCount();
    ValueItem<?> getValueByKey(KeyItem<?> keyItem);
    KeyItem<?> getKey(Object key);
    Map< Object, KeyItem<?> > getAllKeys();
    default Map< Object, KeyItem<?> > getPartKeys(Object ...keys){
        HashMap<Object, KeyItem<?>> map = new HashMap<>();
        for (Object key :
                keys) {
            KeyItem<?> keyItem = getKey(key);
            if(keyItem != null){
                map.put(key, keyItem);
            }
        }
        return map;
    }
    default void checkOutOfBounds(int index){
        int maxCount = getKeyValueCount();
        if(index >= maxCount){
            throw new IndexOutOfBoundsException("文档中最多只有" + maxCount +"个节点，" +
                    "index必须在：[0-" + (maxCount - 1) + "]");
        }
    }

    @Override
    Object output(Object... params);
}
