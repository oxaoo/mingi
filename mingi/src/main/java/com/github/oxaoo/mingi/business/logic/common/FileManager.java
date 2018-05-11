package com.github.oxaoo.mingi.business.logic.common;

import com.github.oxaoo.mingi.data.source.FileReader;
import com.github.oxaoo.mingi.data.source.FileWriter;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 02.05.2017
 */
@Data
@AllArgsConstructor
public class FileManager {
    private FileReader fileReader;
    private FileWriter fileWriter;

    public FileManager() {
        this.fileReader = new FileReader();
        this.fileWriter = new FileWriter();
    }

    public boolean saveFile(InputStream uploadedInputStream) {
        return this.fileWriter.stream2File(uploadedInputStream);
    }

    public boolean saveFile(String text, String path) {
        return this.fileWriter.text2File(text, path);
    }

    public String loadFile(String path) {
        return this.fileReader.file2Text(path);
    }

    public OutputStream loadFile2Stream(String path) {
        return this.fileReader.file2Stream(path);
    }
}
