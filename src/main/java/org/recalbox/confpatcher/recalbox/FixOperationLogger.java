package org.recalbox.confpatcher.recalbox;

public interface FixOperationLogger {

    void logGameFixed(String name);

    void logGameNotFoundInHypersinDatabase(String name);

    void logImageNotFound(String nameFromPath);

}
