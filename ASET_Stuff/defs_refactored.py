import os
from PIL import Image
import random

def process_images(augmentationHolder):
    images_from_location = os.listdir(augmentationHolder.images_path)
    for image_in_directory in images_from_location:
        image_path = os.path.join(augmentationHolder.images_path, image_in_directory)
        image = Image.open(image_path)
        if augmentationHolder.new_size:
            image = image.resize((augmentationHolder.new_size, augmentationHolder.new_size))
        image_name = image_path.split('\\')[-1].split('.')[0]
        
        image = image.crop((augmentationHolder.top_x, 
                            augmentationHolder.top_y, 
                            augmentationHolder.top_x + augmentationHolder.patch_size, 
                            augmentationHolder.top_y + augmentationHolder.patch_size))
        image.save(f"{augmentationHolder.save_path}//{image_name}.tif")
# def process_images(images_path, save_path, new_size, patch_size, top_x, top_y):
#     images_from_location = os.listdir(images_path)
#     for image_in_directory in images_from_location:
#         image_path = os.path.join(images_path, image_in_directory)
#         image = Image.open(image_path)
#         if new_size:
#             image = image.resize((new_size, new_size))
#         image_name = image_path.split('\\')[-1].split('.')[0]
        
#         image = image.crop((top_x, top_y, top_x + patch_size, top_y + patch_size))
#         image.save(f"{save_path}//{image_name}.tif")

# def process_images(images_path, save_path, patch_size):
#     images_from_location = os.listdir(images_path)
#     generated_values = False
#     for image_in_directory in images_from_location:
#         image_path = os.path.join(images_path, image_in_directory)
#         image = Image.open(image_path)
#         image_name = image_path.split('\\')[-1].split('.')[0]
#         if not generated_values:
#             width, height = image.size
#             top_x = random.randint(0, width - patch_size[0])
#             top_y = random.randint(0, height - patch_size[1])
#             bottom_x = top_x + patch_size[0]
#             bottom_y = top_y + patch_size[1]
#             generated_values = True
#         image = image.crop((top_x, top_y, bottom_x, bottom_y))
#         image.save(f"{save_path}//{image_name}.tif")
        
def process_location(augmentationObject, location):
    images_location = os.path.join(augmentationObject.image_path, location, 'images')
    images_from_location = os.listdir(images_location)
    
    location_save_name = f"{augmentationObject.save_path}\\{location}_{augmentationObject.new_size}_{augmentationObject.patch_size}_{augmentationObject.top_x}_{augmentationObject.top_y}\\images"
    while os.path.exists(location_save_name):
        top_x = random.randint(0, augmentationObject.new_size - augmentationObject.patch_size)
        top_y = random.randint(0, augmentationObject.new_size - augmentationObject.patch_size)
        location_save_name = f"{augmentationObject.save_path}\\{location}_{augmentationObject.new_size}_{augmentationObject.patch_size}_{augmentationObject.top_x}_{augmentationObject.top_y}\\images"
    os.mkdir(f"{augmentationObject.save_path}\\{location}_{augmentationObject.new_size}_{augmentationObject.patch_size}_{augmentationObject.top_x}_{augmentationObject.top_y}")
    os.mkdir(location_save_name)
    
    process_images(augmentation)    
    return 1

# def process_location(image_directory, save_directory, patch_size, location, unique_id):
#     images_location = os.path.join(image_directory, location, 'images')
#     images_from_location = os.listdir(images_location)
#     location_save_name = f"{save_directory}\\{location}_{unique_id}\\images"
    
#     create_directory(f"{save_directory}\\{location}_{unique_id}")
#     create_directory(location_save_name)
    
#     process_images(images_location, location_save_name, (patch_size[0], patch_size[1]))    
#     return unique_id
