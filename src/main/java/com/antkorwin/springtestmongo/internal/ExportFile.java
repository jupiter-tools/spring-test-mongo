package com.antkorwin.springtestmongo.internal;

import com.antkorwin.commonutils.exceptions.InternalException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Save text in a file.
 *
 * @author Korovin Anatoliy
 */
class ExportFile {

    private final Text text;

    ExportFile(Text text) {
        this.text = text;
    }

    /**
     * save text in a file, if the file does not exist
     * then it will create with all folders in the path.
     *
     * @param fileName path to file
     */
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
