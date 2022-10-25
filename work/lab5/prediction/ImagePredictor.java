/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PredictionManager;
import java.awt.image.BufferedImage;
import org.tensorflow.SavedModelBundle;

/**
 *
 * @author Tudor Onofrei
 */
class ImagePredictor {
    private String pathToFile;
    private SavedModelBundle model;
    
    ImagePredictor(String newPathToFile){
        // loading model from newPath
    }
    
    public BufferedImage predictInFuture(Integer numberOfYears){
        return new BufferedImage(0, 0, 0);
    }
}
