package tech.argentoaskia.operation.IO;

import tech.argentoaskia.dispatcher.INIFileDispatcher;
import tech.argentoaskia.exception.DoNotRunThisMethodException;
import tech.argentoaskia.models.impl.Document;
import tech.argentoaskia.models.impl.Key;
import tech.argentoaskia.models.impl.Session;
import tech.argentoaskia.models.impl.Value;
import tech.argentoaskia.models.interfaces.DocumentItem;
import tech.argentoaskia.models.interfaces.KeyValueItem;
import tech.argentoaskia.models.interfaces.SessionItem;
import tech.argentoaskia.operation.ReaderOperation;

import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 基本INIFileReader抽象类，当需要编写自己的INIReader实现的时候可以扩展此类。
 * 也可以是使用factory对现有的类方法进行热替换！
 *
 * @author Askia
 * @param <T> 扩展ReaderOperation接口，为了方便后期增加方法，使用该泛型，用于替换原有的方法实现，原理类似于Thread.run()
 */
public abstract class INIFileReader<T extends ReaderOperation> extends Reader implements ReaderOperation, InvocationHandler, INIFileDispatcher {
    // reader 处理问文本会比处理字节流方便
    protected Reader reader;
    // 指定INI文件是如何被解析的，该类本身会实现INIFileDispatcher并提供默认实现，如果指定这个类将会优先使用
    protected static INIFileDispatcher dispatcherImpl;
    // part of operation，效果同INIFileDispatcher，
    protected T operationPartImpl;
    // document Items
    protected DocumentItem<?> document;

    public static void installDispatcherImpl(INIFileDispatcher dispatcherImpl){
        INIFileReader.dispatcherImpl = dispatcherImpl;
    }
    public static void uninstallDispatcherImpl(){
        INIFileReader.dispatcherImpl = null;
    }

    public INIFileReader(String path){
        this(new File(path));
    }
    public INIFileReader(String path, Charset charset){
        this(new File(path), charset);
    }

    public INIFileReader(File filePath, Charset charset){
        try {
            reader = new FileReader(filePath, charset);
        } catch (IOException e) {
            // todo file not found
            e.printStackTrace();
        }
        chooseDispatch(reader, filePath);
    }
    public INIFileReader(File filePath) {
        try {
            reader = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            // todo file not found

        }
        chooseDispatch(reader, filePath);
    }

    public void setOperationPartImpl(T operationPartImpl){
        this.operationPartImpl = operationPartImpl;
    }

    private void chooseDispatch(Reader resource, File fileMessage){
        if(dispatcherImpl != null)  document = dispatcherImpl.dispatchFile(resource, fileMessage);
        else                        document = dispatchFile(resource, fileMessage);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 未安装operationPartImpl，使用默认实现
        if(operationPartImpl == null){
            return invoking(proxy, this, method, args);
        }else{
            // 已安装operationPartImpl，进行方法分流，优先使用operationPartImpl
            // 为了自定义方法分流，需要留下一个抽象方法来进行方法分类
            // 同时为了更加好地处理结果，要留下抽象方法来处理结果
            // 如果在这个过程中抛出异常，要留下抽象方法来处理结果
           try{
               List<Method> runWithImplMethods = MethodDispatch(operationPartImpl, proxy, method, args);
               if(runWithImplMethods == null || runWithImplMethods.size() == 0){
                   // 重新委托this调用
                   return invoking(proxy, this, method, args);
               }
               if(runWithImplMethods.contains(method)){
                   // 包含当前方法使用operationPartImpl
                   return invoking(proxy, operationPartImpl, method, args);
               }else{
                   // 不包含
                   return invoking(proxy, this, method, args);
               }
           }catch (DoNotRunThisMethodException e){
               // 不希望运行某个方法，MethodDispatch抛出IO异常
               return AfterThrow(e, proxy, method, args);
           }
        }
    }
    private Object invoking(Object proxy, ReaderOperation invokeObject, Method method, Object[] args) throws Throwable{
        Object ret = method.invoke(invokeObject, args);
        return AfterInvoke(ret, invokeObject, proxy, method, args);
    }

    protected abstract List<Method> MethodDispatch(T operationPartImpl, Object proxy, Method currentMethod, Object[] args) throws DoNotRunThisMethodException;
    protected Object AfterInvoke(Object retValue, ReaderOperation invokeObject, Object proxy, Method invokeMethod, Object[] args){
        return retValue;
    }
    protected Object AfterThrow(DoNotRunThisMethodException e, Object proxy, Method currentMethod, Object[] args){
        return null;
    }

    // default implement

    /**
     * 自定义的Reader可以重写这个方法来自定义外部INI文件的解析方式
     *
     * @param resource 外部的INI文件读入到JavaIO流变量
     * @param fileMessage 外部INI文件的位置
     * @return 完整的一个Document对象
     */
    @Override
    public Document dispatchFile(Reader resource, File fileMessage)  {
        String line;
        Document document = new Document(fileMessage);
        Session sessionTraver = null;
        Key keyTraver;
        try{
            // 基础算法
            BufferedReader reader = new BufferedReader(resource);
            Value valueTraver;
            while((line = reader.readLine()) != null){
                if(line.equals("")){
                    continue;
                }
                if(line.startsWith("[") && line.endsWith("]")){
                    if(sessionTraver != null){
                        document.putValue(sessionTraver);
                    }
                    String sessionName = line.substring(1, line.length() - 1);
                    sessionTraver = new Session(sessionName);
                } else if(line.contains("=")){
                    if(sessionTraver == null){
                        throw new NullPointerException("sessionTraver in keyValue");
                    }
                    String[] keyvalue = line.split("=");
                    keyTraver = new Key(keyvalue[0]);
                    valueTraver = new Value(keyvalue[1]);
                    sessionTraver.putKeyValue(keyTraver,valueTraver);
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return document;
    }

    /**
     * Reads characters into a portion of an array.  This method will block
     * until some input is available, an I/O error occurs, or the end of the
     * stream is reached.
     *
     * @param cbuf Destination buffer
     * @param off  Offset at which to start storing characters
     * @param len  Maximum number of characters to read
     * @return The number of characters read, or -1 if the end of the
     * stream has been reached
     * @throws IOException               If an I/O error occurs
     * @throws IndexOutOfBoundsException If {@code off} is negative, or {@code len} is negative,
     *                                   or {@code len} is greater than {@code cbuf.length - off}
     */
    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        return reader.read(cbuf, off, len);
    }

    @Override
    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            // todo IOException on close
            e.printStackTrace();
        }
    }

    /**
     * 查询配置项的节点数
     *
     * @return 节点数
     */
    @Override
    public int getSessionCount() {
        return 0;
    }

    /**
     * 查询节点内的键值对数
     *
     * @param targetSession 目标节点
     * @return 键值对数量
     */
    @Override
    public <S> int getSessionKeyValueCount(SessionItem<S> targetSession) {
        return 0;
    }

    /**
     * 该方法会调用{@link ReaderOperation#getSession(S)}和
     * {@link ReaderOperation#getSessionKeyValueCount(SessionItem)}来获取键值对数量
     *
     * @param targetSessionName 目标节点
     * @return 键值对数量
     * @see ReaderOperation#getSession(S)
     * @see ReaderOperation#getSessionKeyValueCount(SessionItem)
     */
    @Override
    public <S> int getSessionKeyValueCount(S targetSessionName) {
        return 0;
    }

    /**
     * 根据名称来获取一个节点
     *
     * @param sessionName 节点名字
     * @return 返回节点
     */
    @Override
    public <S> SessionItem<S> getSession(S sessionName) {
        return null;
    }

    /**
     * 根据下标来获取一个节点
     *
     * @param index 下标，从0开始
     * @return 返回节点
     */
    @Override
    public <S> SessionItem<S> getSession(int index) {
        return null;
    }

    /**
     * 获取部分节点，改方法会多次调用{@link ReaderOperation#getSession(S)}
     *
     * @param sessionNames 多个节点名字
     * @return 一个Map，用于存储节点
     * @see ReaderOperation#getSession(S)
     */
    @SafeVarargs
    @Override
    public final <S> Map<S, SessionItem<S>> getSessions(S... sessionNames) {
        return null;
    }

    @Override
    public SessionItem<?>[] getAllSessions() {
        return new SessionItem[0];
    }

    @Override
    public <S, K> KeyValueItem<K> getKey(SessionItem<S> targetSession, int index) {
        return null;
    }

    @Override
    public <S, K> KeyValueItem<K> getKey(S targetSessionName, int index) {
        return null;
    }

    @Override
    public <S, K> KeyValueItem<K> getKey(S targetSessionName, K Key) {
        return null;
    }

    @Override
    public <S, K> KeyValueItem<K> getKey(SessionItem<S> targetSession, K Key) {
        return null;
    }

    @Override
    public <S> KeyValueItem<?>[] getKeys(SessionItem<S> targetSession) {
        return new KeyValueItem[0];
    }

    @Override
    public <S> KeyValueItem<?>[] getKeys(S targetSessionName) {
        LinkedList<KeyValueItem<?>> keyValueItems = new LinkedList<>();

        return keyValueItems.toArray(new KeyValueItem[0]);
    }

    @Override
    public KeyValueItem<?>[] getAllKeys() {
        return new KeyValueItem[0];
    }

    @Override
    public <D> DocumentItem<D> getDocument() {
        return null;
    }

    @Override
    public <S, K, V> KeyValueItem<V> getValue(SessionItem<S> targetSession, KeyValueItem<K> key) {
        return null;
    }

    @Override
    public <S, K, V> KeyValueItem<V> getValue(SessionItem<S> targetSession, K key) {
        return null;
    }

    @Override
    public <S, K, V> KeyValueItem<V> getValue(S targetSession, K key) {
        return null;
    }

    @Override
    public <S, K, V> KeyValueItem<V> getValue(S targetSession, KeyValueItem<K> key) {
        return null;
    }
}
