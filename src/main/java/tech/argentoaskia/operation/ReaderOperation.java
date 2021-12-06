package tech.argentoaskia.operation;

import tech.argentoaskia.models.impl.Document;
import tech.argentoaskia.models.interfaces.DocumentItem;
import tech.argentoaskia.models.interfaces.KeyValueItem;
import tech.argentoaskia.models.interfaces.SessionItem;

import java.util.HashMap;

public interface ReaderOperation extends Operation{
    /**
     * 查询配置项的节点数
     * @return 节点数
     */
    int getSessionCount();

    /**
     * 查询节点内的键值对数
     * @param targetSession 目标节点
     * @param <S> 节点名称类型
     * @return 键值对数量
     */
    <S> int getSessionKeyValueCount(SessionItem<S> targetSession);

    /**
     * 该方法会调用{@link ReaderOperation#getSession(S)}和
     * {@link ReaderOperation#getSessionKeyValueCount(SessionItem)}来获取键值对数量
     *
     * @param targetSessionName 目标节点
     * @param <S> 节点名称的类型
     * @return 键值对数量
     * @see ReaderOperation#getSession(S)
     * @see ReaderOperation#getSessionKeyValueCount(SessionItem)
     */
    <S> int getSessionKeyValueCount(S targetSessionName);

    /**
     * 根据名称来获取一个节点
     * @param sessionName 节点名字
     * @param <S> 节点名称的类型
     * @return 返回节点
     */
    <S> SessionItem<S> getSession(S sessionName);
    
    <S> SessionItem<S> getSession(int index);


    <S> HashMap<S, SessionItem<S>> getSessions(S ...sessionNames);
    <S> SessionItem<S>[] getAllSessions();

    <S, K> KeyValueItem<K> getKey(SessionItem<S> targetSession, int index);
    <S, K> KeyValueItem<K> getKey(S targetSessionName, int index);
    <S, K> KeyValueItem<K> getKey(S targetSessionName, K Key);
    <S, K> KeyValueItem<K> getKey(SessionItem<S> targetSession, K Key);
    <S, K> KeyValueItem<K>[] getKeys(SessionItem<S> targetSession);
    <S, K> KeyValueItem<K>[] getKeys(S targetSessionName);
    <K> KeyValueItem<K>[] getAllKeys();

    <D> DocumentItem<D> getDocument();


    <S, K, V> KeyValueItem<V> getValue(SessionItem<S> targetSession, KeyValueItem<K> key);
    <S, K, V> KeyValueItem<V> getValue(SessionItem<S> targetSession, K key);
    <S, K, V> KeyValueItem<V> getValue(S targetSession, K key);
    <S, K, V> KeyValueItem<V> getValue(S targetSession, KeyValueItem<K> key);
}
