package com.jupiter.tools.spring.test.mongo.internal.importdata;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.antkorwin.commonutils.exceptions.InternalException;
import com.jupiter.tools.spring.test.mongo.internal.Text;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.jupiter.tools.spring.test.mongo.errorinfo.MongoDbErrorInfo.READ_DATASETS_FILE_ERROR;

/**
 * Load text from a file.
 *
 * @author Korovin Anatoliy
 */
public class ImportFile implements Text {

    private final String fileName;
    private final Logger log = LoggerFactory.getLogger(ImportFile.class);

    public ImportFile(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String read() {
        try (InputStream inputStream = getResourceStream()) {
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        }
        catch (Exception e) {
            log.error("Error while trying to read data from file: {}", fileName, e);
            throw new InternalException(READ_DATASETS_FILE_ERROR, e);
        }
    }

    private InputStream getResourceStream() throws IOException {
        String dataFileName = (!fileName.startsWith("/"))
                              ? "/" + fileName
                              : fileName;

        InputStream inputStream = getClass().getResourceAsStream(dataFileName);
        if (inputStream == null) {
            inputStream = getClass().getResourceAsStream("/dataset" + dataFileName);
        }
        if(inputStream == null) {
            inputStream = Files.newInputStream(Paths.get(fileName));
        }
        return inputStream;
    }
}
