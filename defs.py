import os
from PIL import Image
import random
from mpire import WorkerPool

def process_images(images_path, save_path, patch_size):
    images_from_location = os.listdir(images_path)
    generated_values = False
    for image_in_directory in images_from_location:
        image_path = os.path.join(images_path, image_in_directory)
        image = Image.open(image_path)
        image_name = image_path.split('\\')[-1].split('.')[0]
        if not generated_values:
            width, height = image.size
            top_x = random.randint(0, width - patch_size[0])
            top_y = random.randint(0, height - patch_size[1])
            bottom_x = top_x + patch_size[0]
            bottom_y = top_y + patch_size[1]
            generated_values = True
        image = image.crop((top_x, top_y, bottom_x, bottom_y))
        image.save(f"{save_path}//{image_name}_{patch_size[0]}_{patch_size[1]}.tif")


def process_location(image_directory, save_directory, patch_size, location):
    images_location = os.path.join(image_directory, location, 'images')
    images_from_location = os.listdir(images_location)
    location_save_name = f"{save_directory}\\{location}_{len(os.listdir(save_directory))}\\images"
    
    os.mkdir(f"{save_directory}\\{location}_{len(os.listdir(save_directory))}")
    os.mkdir(location_save_name)
    
    process_images(images_location, location_save_name, (patch_size[0], patch_size[1]))    


def augment_dataset_one_time(image_directory, save_directory, patch_size):
    location_directories = os.listdir(image_directory)
    for location in location_directories:
        process_location(image_directory, save_directory, patch_size, location)
