package tech.argentoaskia.dispatcher;

import tech.argentoaskia.models.impl.Document;

import java.io.File;
import java.io.Reader;

/**
 * 本接口在对INI文件进行扩展时，即自定义INI文件的元素时，可以对方法进行重写
 *
 */
public interface INIFileDispatcher {

    Document dispatchFile(Reader resource, File fileMessage);
}
