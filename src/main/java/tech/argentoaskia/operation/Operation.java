package tech.argentoaskia.operation;

import tech.argentoaskia.models.interfaces.DocumentItem;

public interface Operation {

    DocumentItem<?> getDocument();
}
