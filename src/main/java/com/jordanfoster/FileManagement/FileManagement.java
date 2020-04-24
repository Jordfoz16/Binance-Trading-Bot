package com.jordanfoster.FileManagement;

import java.io.*;

public class FileManagement {

    public void write(String path, String content) {
        String fileName = path;

        try {
            FileWriter fileWriter = new FileWriter(fileName);

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(content);

            bufferedWriter.close();
        } catch (IOException ex) {

        }
    }

    public String read(String path) {

        String fileName = path;

        String line;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            bufferedReader.close();

            return stringBuilder.toString();
        } catch (FileNotFoundException ex) {

        } catch (IOException ex) {

        }

        return null;
    }

    public boolean fileExists(String path){
        File file = new File(path);
        return file.exists();
    }
}
