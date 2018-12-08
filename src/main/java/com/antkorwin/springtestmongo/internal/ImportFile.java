package com.antkorwin.springtestmongo.internal;

import com.antkorwin.commonutils.exceptions.InternalException;
import com.antkorwin.commonutils.validation.Guard;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.antkorwin.springtestmongo.errorinfo.MongoDbErrorInfo.FILE_NOT_FOUND;
import static com.antkorwin.springtestmongo.errorinfo.MongoDbErrorInfo.READ_DATASETS_FILE_ERROR;

/**
 * Load text from a file.
 *
 * @author Korovin Anatoliy
 */
class ImportFile implements Text {

    private final String fileName;

    ImportFile(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String read() {
        try (InputStream inputStream = getResourceStream()) {
            Guard.check(inputStream != null, InternalException.class, FILE_NOT_FOUND);
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalException(READ_DATASETS_FILE_ERROR, e);
        }
    }

    private InputStream getResourceStream() {
        return ImportFile.class.getClass()
                               .getResourceAsStream(this.fileName);
    }

}
