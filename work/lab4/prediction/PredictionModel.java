/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PredictionManager;

/**
 *
 * @author Tudor Onofrei
 */
public class PredictionModel {
    static String pathToImagePredictor;
    static String pathToPricePredictor;
    
    static ImagePredictor imagePredictor;
    static PricePredictor pricePredictor;
    
    static ImagePredictor getImagePredictor(){
        if(imagePredictor == null){
            imagePredictor = new ImagePredictor(pathToImagePredictor);
        }
        return imagePredictor;
    }
    
    static PricePredictor getPricePredictor(){
        if(pricePredictor == null){
            pricePredictor = new PricePredictor(pathToPricePredictor);
        }
        return pricePredictor;
    }
}
