package tech.argentoaskia.models.interfaces;

import tech.argentoaskia.models.Item;
import tech.argentoaskia.models.impl.Document;
import tech.argentoaskia.models.impl.Key;
import tech.argentoaskia.models.impl.Session;
import tech.argentoaskia.models.impl.Value;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

public abstract class DocumentItem<T> implements Item <File>{
    protected File path;
    protected HashMap<T, SessionItem<T>> sessionHashMap;

    public DocumentItem(File path){
        this.path = path;
        sessionHashMap = new HashMap<>();
    }

    public DocumentItem(String strPath){
        path = new File(strPath);
        sessionHashMap = new HashMap<>();
    }

    public Set<T> keySet(){
        return sessionHashMap.keySet();
    }
    public SessionItem<T> getValue(T sessionName){
        return sessionHashMap.get(sessionName);
    }
    public int keyCount(){
        return sessionHashMap.size();
    }
    public SessionItem<T> putValue(SessionItem<T> sessionItem){
        T sessionName = sessionItem.get();
        return sessionHashMap.put(sessionName, sessionItem);
    }
    @Override
    public File get() {
        return path;
    }

}
