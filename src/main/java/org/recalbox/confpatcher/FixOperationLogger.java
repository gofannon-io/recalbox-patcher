package org.recalbox.confpatcher;

public interface FixOperationLogger {

    void logGameFixed(String name);

    void logGameNotFoundInHypersinDatabase(String name);

    void logImageNotFound(String nameFromPath);

}
