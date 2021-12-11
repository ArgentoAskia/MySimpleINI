package tech.argentoaskia.operation;


import tech.argentoaskia.models.interfaces.DocumentItem;

import java.io.File;
import java.io.Reader;

public interface ReaderOperation extends Operation{

    DocumentItem<?> dispatch(Reader fileStream, File file);
}
