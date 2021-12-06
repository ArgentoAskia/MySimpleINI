package tech.argentoaskia.operation.IO;

import tech.argentoaskia.dispatcher.INIFileDispatcher;
import tech.argentoaskia.models.impl.Document;
import tech.argentoaskia.models.impl.Key;
import tech.argentoaskia.models.impl.Session;
import tech.argentoaskia.models.impl.Value;
import tech.argentoaskia.models.interfaces.DocumentItem;
import tech.argentoaskia.operation.ReaderOperation;

import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.nio.charset.Charset;

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
    protected INIFileDispatcher dispatcherImpl;
    // part of operation
    protected T operationPartImpl;
    protected DocumentItem<?> document;

    INIFileReader(INIFileDispatcher dispatcherImpl, T operationPartImpl){
        installImpls(dispatcherImpl, operationPartImpl);
    }
    void installImpls(INIFileDispatcher dispatcherImpl, T operationPartImpl){
        this.dispatcherImpl = dispatcherImpl;
        this.operationPartImpl = operationPartImpl;
    }
    void uninstallImples(){
        this.dispatcherImpl = null;
        this.operationPartImpl = null;
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

    private void chooseDispatch(Reader resource, File fileMessage){
        if(dispatcherImpl != null)  document = dispatcherImpl.dispatchFile(resource, fileMessage);
        else                        document = dispatchFile(resource, fileMessage);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
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
        Key keyTraver = null;
        Value valueTraver = null;
        try(BufferedReader reader = new BufferedReader(resource)){
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

    @Override
    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            // todo IOException on close
            e.printStackTrace();
        }
    }


}
