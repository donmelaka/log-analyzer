package com.melaka;

import java.io.File;

public interface TestDataLoadable {

    default String getFilePath(final String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();

        File file = new File(classLoader.getResource(fileName).getFile());
        return file.getAbsolutePath();
    }
}
