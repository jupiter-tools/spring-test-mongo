package com.antkorwin.springtestmongo.internal;

import com.antkorwin.commonutils.exceptions.InternalException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class FileData {

    private final TextData textData;

    public FileData(TextData textData) {
        this.textData = textData;
    }

    void write(String fileName) {
        String textData = this.textData.read();
        try {
            Path path = Paths.get(fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, textData.getBytes());
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new InternalException(e);
        }
    }
}