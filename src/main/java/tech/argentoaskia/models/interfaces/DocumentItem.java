package tech.argentoaskia.models.interfaces;

import tech.argentoaskia.models.Item;

import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

public interface DocumentItem<D> extends Item<D> {

    int getSessionCount();
    <S> SessionItem<S> getSession(Object sessionString, Class<S> typeForSessionData);
    <S> SessionItem<S> getSession(int index, Class<S> typeForSessionData);
    <S> Map<Object, SessionItem<S>> getAllSessions();
}
