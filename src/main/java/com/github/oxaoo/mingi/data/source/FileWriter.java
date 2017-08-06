package com.github.oxaoo.mingi.data.source;

import com.github.oxaoo.mingi.business.logic.common.PropertyManager;
import com.sun.jersey.core.header.FormDataContentDisposition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 02.05.2017
 */
public class FileWriter {
    private static final Logger LOG = LoggerFactory.getLogger(FileWriter.class);

    private final static String UPLOAD_RESOURCE_DIRECTORY_PROPERTY = "upload.resource.directory";

    public boolean stream2File(InputStream uploadedInputStream, FormDataContentDisposition fileDetail) {
        String filePath = this.resolvePath(fileDetail);
        this.deleteIf(filePath);

        byte[] bytes = new byte[1024];
        OutputStream out;
        int read;
        try {
            out = new FileOutputStream(new File(filePath));
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            LOG.error("Filed to write the stream to file. Cause: {}", e.getMessage());
            return false;
        }
        return true;
    }

    public boolean text2File(String text, String path) {
        //todo stub
        return false;
    }

    private void deleteIf(String filePath) {
        File uploadFile = new File(filePath);
        if (uploadFile.exists()) uploadFile.delete();
    }

    private String resolvePath(FormDataContentDisposition fileDetail) {
        return PropertyManager.getProperty(UPLOAD_RESOURCE_DIRECTORY_PROPERTY) + fileDetail.getFileName();
    }
}
