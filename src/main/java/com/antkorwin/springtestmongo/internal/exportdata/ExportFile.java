package com.antkorwin.springtestmongo.internal.exportdata;

import com.antkorwin.commonutils.exceptions.InternalException;
import com.antkorwin.springtestmongo.internal.Text;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Save text in a file.
 *
 * @author Korovin Anatoliy
 */
public class ExportFile {

    private final Text text;

    public ExportFile(Text text) {
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
        } catch (Exception e) {
            LoggerFactory.getLogger(ExportFile.class)
                         .error("Error while save text in file", e);
            throw new InternalException(e);
        }
    }
}
