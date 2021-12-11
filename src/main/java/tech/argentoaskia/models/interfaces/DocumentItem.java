package tech.argentoaskia.models.interfaces;

import tech.argentoaskia.models.Item;

import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

public interface DocumentItem<T> extends Item<T> {

    int getSessionCount();
    <S> SessionItem<S> getSession(Object judgeObj);
    <S> Map< Object, SessionItem<S> > getAllSessions();
    default void checkOutOfBounds(int index){
        int maxCount = getSessionCount();
        if(index >= maxCount){
            throw new IndexOutOfBoundsException("文档中最多只有" + maxCount +"个节点，" +
                    "index必须在：[0-" + (maxCount - 1) + "]");
        }
    }
    default <S> Map< Object, SessionItem<S> > getPartSession(Object ...judgeObjs){
        HashMap<Object, SessionItem<S>> map = new HashMap<>();
        for (Object jObj :
                judgeObjs) {
            SessionItem<S> sSessionItem = getSession(judgeObjs);
            if(sSessionItem != null){
                map.put(jObj, sSessionItem);
            }
        }
        return map;
    }

    @Override
    RandomAccessFile output(Object... mode);
}
