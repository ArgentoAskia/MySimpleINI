package tech.argentoaskia.operation.IO.impl;

import tech.argentoaskia.models.interfaces.DocumentItem;
import tech.argentoaskia.models.interfaces.SessionItem;
import tech.argentoaskia.operation.ReaderOperation;

import java.io.*;
import java.nio.charset.Charset;

/**
 * 基本INIFileReader抽象类，当需要编写自己的INIReader实现的时候可以扩展此类。
 * 也可以是使用factory对现有的类方法进行热替换！
 *
 * @author Askia
 */
public class INIFileReaderBaseImpl extends Reader implements ReaderOperation{
    // reader 处理问文本会比处理字节流方便
    protected Reader reader;
    // part of operation，效果同INIFileDispatcher.
    protected static ReaderOperation operationPartImpl;
    // document Items
    protected DocumentItem<?> document;

    public static void installDispatcherImpl(ReaderOperation dispatcherImpl){
        INIFileReaderBaseImpl.operationPartImpl = dispatcherImpl;
    }
    public static void uninstallDispatcherImpl(){
        INIFileReaderBaseImpl.operationPartImpl = null;
    }

    public INIFileReaderBaseImpl(String path){
        this(new File(path));
    }
    public INIFileReaderBaseImpl(String path, Charset charset){
        this(new File(path), charset);
    }

    public INIFileReaderBaseImpl(File filePath, Charset charset){
        try {
            reader = new FileReader(filePath, charset);
        } catch (IOException e) {
            // todo file not found
            e.printStackTrace();
        }
        chooseDispatch(reader, filePath);
    }
    public INIFileReaderBaseImpl(File filePath) {
        try {
            reader = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            // todo file not found

        }
        chooseDispatch(reader, filePath);
    }


    private void chooseDispatch(Reader resource, File fileMessage){
        if(operationPartImpl != null)  document = operationPartImpl.dispatch(resource, fileMessage);
        else                        document = dispatch(resource, fileMessage);
    }

    // default implement

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


    @Override
    public DocumentItem<?> dispatch(Reader fileStream, File file) {
        return null;
    }

    private <T> SessionItem<T> solveSession(StringBuilder stringBuilder){
        return null;
    }

    @Override
    public DocumentItem<?> getDocument() {
        return document;
    }
}
