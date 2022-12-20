package com.tensorreloaded.propertyprediction.model;

import com.tensorreloaded.propertyprediction.rest.controllers.ImageUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class ModelUsage {
    public String predict(String imageB64) {
        try {
            var is = new ProcessBuilder(
                    "python3",
                    "/Users/tudorcraciun/VisualStudioCode/Property-Development-Prediction/work/lab5/PDP_api/src/main/resources/run_model.py"
            ).start().getInputStream();
            // modificat in:
            //            var is = new ProcessBuilder(
            //                    "path_windows_python3",
            //                    "path_script",
            //                    "base64"
            //            ).start().getInputStream();
            var isr = new InputStreamReader(is);
            var br = new BufferedReader(isr);
            return br.readLine();
        } catch (IOException e) {
            return new ImageUtil().flipBase64image(
                    imageB64
            );
        }
    }
}
