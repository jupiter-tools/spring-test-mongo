package com.antkorwin.springtestmongo.internal;

import com.antkorwin.commonutils.exceptions.InternalException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created on 06.12.2018.
 *
 * @author Korovin Anatoliy
 */
class ExportFile {

    private final Text text;

    public ExportFile(Text text) {
        this.text = text;
    }

    public void write(String fileName) {
        String textData = this.text.read();
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
