/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PredictionManager;

import org.tensorflow.SavedModelBundle;

/**
 *
 * @author Tudor Onofrei
 */
public class PricePredictor {
    private String pathToFile;
    private SavedModelBundle model;
    
    PricePredictor(String newPathToFile){
        // loading model from newPath
    }
    
    public Integer predictInFuture(Integer numberOfYears){
        return 0;
    }
}
