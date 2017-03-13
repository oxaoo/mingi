package com.github.oxaoo.qas.qa;

import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mp4ru.syntax.RussianParser;

public class Main {
    public static void main(String[] args) throws FailedParsingException {

        String parserDir = "src/main/resources/parser/";
        String textFilePath = parserDir + "text.txt";
        String classifierModel = parserDir + "russian-utf8.par";
        String resultParseFile = new RussianParser().parsing(textFilePath, classifierModel, parserDir, parserDir);
        System.out.println("Res parse file: " + resultParseFile);
    }
}
