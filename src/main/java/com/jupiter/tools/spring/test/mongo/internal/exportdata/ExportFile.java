package com.jupiter.tools.spring.test.mongo.internal.exportdata;

import com.antkorwin.commonutils.exceptions.InternalException;
import com.jupiter.tools.spring.test.mongo.internal.Text;
import org.slf4j.Logger;
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
    private final Logger log;

    public ExportFile(Text text) {
        this.text = text;
        this.log = LoggerFactory.getLogger(ExportFile.class);
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
            log.error("Error while save text in file", e);
            throw new InternalException(e);
        }
    }
}
