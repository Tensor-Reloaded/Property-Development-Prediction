import java.util.HashMap;
import java.util.Map;

public class ImagePreProcessorFactory {

    private static ImagePreProcessorFactory instance;

    private Map<ImagePreProcessorType, ImagePreProcessor> imagePreProcessorMap;

    private ImagePreProcessorFactory() {

        imagePreProcessorMap = new HashMap<>();
        imagePreProcessorMap.put(ImagePreProcessorType.CONTRAST_PROCESSOR, new ImageContrastPreProcessor());
        imagePreProcessorMap.put(ImagePreProcessorType.SIZE_PROCESSOR, new ImageSizePreProcessor());
    }

    public static ImagePreProcessorFactory getInstance() {
        if(instance == null) {
            instance = new ImagePreProcessorFactory();
        }
        return instance;
    }

    public ImagePreProcessor getImagePreProcessor(ImagePreProcessorType type) {

        /// to modify
        return imagePreProcessorMap.get(type);
    }
}
