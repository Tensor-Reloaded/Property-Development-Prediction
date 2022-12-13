#!/usr/bin/env python
# coding: utf-8

# In[1]:


from multiprocessing import Pool
import defs
import os
from math import ceil
from PIL import Image
import numpy as np


class AugmentationParameters:
    def __init__(self, images_path, save_path, new_size, patch_size, top_x, top_y):
        self.image_path = image_path
        self.save_path = save_path
        self.new_size = new_size
        self.patch_size = patch_size
        self.top_x = top_x
        self.top_y = top_y

if __name__ == '__main__':
    directory = "E:\\Facultate\\_Master\\Advanced Software Engineering Techniques\\Dataset_SpaceNet\\SN7_buildings_train\\train"
    save_directory = "E:\\Facultate\\_Master\\Advanced Software Engineering Techniques\\train_augment"
    patch_size = 128
    new_size = 1024
    top_x = np.random.randint(0, new_size - patch_size, total_locations * augmentation_number)
    top_y = np.random.randint(0, new_size - patch_size, total_locations * augmentation_number)
    parameters = []
    for i in range(total_locations * augmentation_number):
        parameters.append(new AugmentationParameters(directory, save_directory, new_size, patch_size, top_x, top_y))
    
    processes_number = 5
    augmentation_number = 2
    total_locations = len(os.listdir(directory))
    
    work_portion_size = ceil(total_locations // processes_number) * augmentation_number
    
    locations = os.listdir(directory)
    locations = locations * augmentation_number
    
    argument_list = [[parameters[i], locations[i % 120]] for i in range(total_locations * augmentation_number)] 
    
    result = []
    print("Started jobs")
    pool = Pool(processes=processes_number, maxtasksperchild=work_portion_size)
    result = pool.starmap(defs.process_location, argument_list)
    pool.close()
    pool.join()

    print(result)
    print("Pool closed")
    

# if __name__ == '__main__':
#     directory = "E:\\Facultate\\_Master\\Advanced Software Engineering Techniques\\Dataset_SpaceNet\\SN7_buildings_train\\train"
#     save_directory = "E:\\Facultate\\_Master\\Advanced Software Engineering Techniques\\train_augment"
#     patch_size = 256
#     new_size = 256
    
#     processes_number = 5
#     augmentation_number = 2
#     total_locations = len(os.listdir(directory))
    
#     work_portion_size = ceil(total_locations // processes_number) * augmentation_number
    
#     locations = os.listdir(directory)
#     locations = locations * augmentation_number
    
#     top_x = np.random.randint(0, new_size - patch_size, total_locations * augmentation_number)
#     top_y = np.random.randint(0, new_size - patch_size, total_locations * augmentation_number)
    
#     argument_list = [[directory, save_directory, new_size, patch_size, top_x[i], top_y[i], locations[i % 120]] for i in range(total_locations * augmentation_number)] 
    
#     result = []
#     print("Started jobs")
#     pool = Pool(processes=processes_number, maxtasksperchild=work_portion_size)
#     result = pool.starmap(defs.process_location, argument_list)
#     pool.close()
#     pool.join()

#     print(result)
#     print("Pool closed")


# In[ ]:




