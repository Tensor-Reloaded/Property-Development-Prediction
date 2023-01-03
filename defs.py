import os
from PIL import Image
import random

def process_images(images_path, save_path, new_size, patch_size, top_x, top_y):
    images_from_location = os.listdir(images_path)
    for image_in_directory in images_from_location:
        image_path = os.path.join(images_path, image_in_directory)
        image = Image.open(image_path)
        if new_size:
            image = image.resize((new_size, new_size))
        image_name = image_path.split('\\')[-1].split('.')[0]
        
        image = image.crop((top_x, top_y, top_x + patch_size, top_y + patch_size))
        image.save(f"{save_path}//{image_name}.tif")

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
        
def process_location(directory, save_directory, new_size, patch_size, top_x, top_y, location):
    images_location = os.path.join(directory, location, 'images')
    images_from_location = os.listdir(images_location)
    
    location_save_name = f"{save_directory}\\{location}_{new_size}_{patch_size}_{top_x}_{top_y}\\images"
    while os.path.exists(location_save_name):
        top_x = random.randint(0, new_size - patch_size)
        top_y = random.randint(0, new_size - patch_size)
        location_save_name = f"{save_directory}\\{location}_{new_size}_{patch_size}_{top_x}_{top_y}\\images"
    os.mkdir(f"{save_directory}\\{location}_{new_size}_{patch_size}_{top_x}_{top_y}")
    os.mkdir(location_save_name)
    
    process_images(images_location, location_save_name, new_size, patch_size, top_x, top_y)    
    return 1

# def process_location(image_directory, save_directory, patch_size, location, unique_id):
#     images_location = os.path.join(image_directory, location, 'images')
#     images_from_location = os.listdir(images_location)
#     location_save_name = f"{save_directory}\\{location}_{unique_id}\\images"
    
#     create_directory(f"{save_directory}\\{location}_{unique_id}")
#     create_directory(location_save_name)
    
#     process_images(images_location, location_save_name, (patch_size[0], patch_size[1]))    
#     return unique_id


def augment_dataset_one_time(image_directory, save_directory, patch_size):
    location_directories = os.listdir(image_directory)
    for location in location_directories:
        process_location(image_directory, save_directory, patch_size, location)