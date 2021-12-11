package tech.argentoaskia.models.base;

import tech.argentoaskia.models.interfaces.DocumentItem;
import tech.argentoaskia.models.interfaces.SessionItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.*;

public class DocumentItemBaseImpl implements DocumentItem<File> {
    private File docFile;
    // 这里的String来源自SessionItem中的toString();
    private HashMap<String, SessionItem<String>> map;
    private LinkedList<String> mapKeys;

    @Override
    public int getSessionCount() {
        return map.size();
    }

    @Override
    public <S> SessionItem<S> getSession(Object judgeObj) {
        String key;
        // 按照顺序来搜索
        if(judgeObj instanceof Integer){
            key = mapKeys.get((Integer) judgeObj);
        }else{
            key = judgeObj.toString();
        }
        return (SessionItem<S>) map.get(key);
    }

    @Override
    public <S> Map<Object, SessionItem<S>> getAllSessions() {
        HashMap<Object, SessionItem<S>> itemHashMap = new HashMap<>();
        Set<String> set = map.keySet();
        for (String s :
                set) {
            SessionItem<S> sSessionItem = getSession(s);
            if(sSessionItem != null){
                itemHashMap.put(s,sSessionItem);
            }
        }
        return itemHashMap;
    }

    /**
     * 该方法将需要返回元素的关键值.
     * <p>
     * 所谓关键值就是指一个元素节点或者接口的关键数据，如：对于一个<code>key</code>来说，
     * 关键值就是它的key值，也就是等号左边的内容，对于一个<code>session</code>来说，
     * 关键值就是它的session名，也就是中括号里面的内容。
     *
     * @return 元素关键值
     */
    @Override
    public File get() {
        return docFile;
    }

    @Override
    public RandomAccessFile output(Object... mode) {
        try {
            return new RandomAccessFile(docFile, (String) mode[0]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public DocumentItemBaseImpl(){
        map = new HashMap<>();
        mapKeys = new LinkedList<>();
    }
    public DocumentItemBaseImpl(File file){
        this();
        docFile = file;
    }
    public DocumentItemBaseImpl(String str){
        this();
        docFile = new File(str);
    }

    public void setDocFile(File file){
        this.docFile = file;
    }
    public void put(SessionItem<String> item){
        map.put(item.toString(), item);
    }
    public void remove(SessionItem<String> item){
        map.remove(item.toString(),item);
    }
}
