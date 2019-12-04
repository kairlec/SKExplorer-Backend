package com.kairlec.utils.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class GetFileContent {
    public static String byPath(String Path) {
        return byFile(new File(Path));
    }

    public static String byFile(File file) {
        if (!file.exists()) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        try (
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
        ) {
            while (!bufferedReader.ready()) {
                Thread.sleep(10);
            }
            String tempStr;
            while ((tempStr = bufferedReader.readLine()) != null) {
                stringBuilder.append(tempStr);
                stringBuilder.append(System.lineSeparator());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "";
        }
        return stringBuilder.toString();
    }
    
}
