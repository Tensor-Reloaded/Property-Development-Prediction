package com.tensorreloaded.propertyprediction.model;

import com.tensorreloaded.propertyprediction.rest.controllers.ImageUtil;

import java.io.*;


public class ModelUsage {
    private final String pathFile = ".\\src\\main\\resources\\Model\\base64.txt";
    private final String pathToPythonScript = ".\\src\\main\\resources\\Model\\ASET_model.py";

    public String predict(String imageB64, int years) {
        File fileBase64 = new File(pathFile);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileBase64))) {
            writer.write(imageB64);
            Process process = new ProcessBuilder(
                    "python",
                    pathToPythonScript,
                    pathFile,
                    String.valueOf(years)
            ).start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            return reader.readLine();
        } catch (IOException e) {
            System.out.println("Exception while reading from files! Message: " + e.getMessage());
            return new ImageUtil().flipBase64image(
                    imageB64
            );
        } catch (Exception e) {
            System.out.println("Unhandled exception! Printing stacktrace." + e.getMessage());
            e.printStackTrace();
            return new ImageUtil().flipBase64image(
                    imageB64
            );
        }
    }

    private void dumpErrorStream(Process process) throws IOException {
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String data;
        while ((data = errorReader.readLine()) != null) {
            System.out.println("[ERROR]" + data);
        }
    }
}
