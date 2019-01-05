package com.antkorwin.springtestmongo.internal.importdata;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import com.antkorwin.commonutils.exceptions.InternalException;
import com.antkorwin.commonutils.validation.Guard;
import com.antkorwin.springtestmongo.internal.Text;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.antkorwin.springtestmongo.errorinfo.MongoDbErrorInfo.FILE_NOT_FOUND;
import static com.antkorwin.springtestmongo.errorinfo.MongoDbErrorInfo.READ_DATASETS_FILE_ERROR;

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

    private InputStream getResourceStream() {
        String dataFileName = (!fileName.startsWith("/"))
                              ? "/" + fileName
                              : fileName;

        InputStream inputStream = getClass().getResourceAsStream(dataFileName);
        if (inputStream == null) {
            inputStream = getClass().getResourceAsStream("/dataset" + dataFileName);
        }
        return inputStream;
    }
}
