#!/usr/bin/env python
# coding: utf-8

# In[ ]:


from multiprocessing import Pool
import defs
import os
from PIL import Image

if __name__ == '__main__':
    directory = "E:\\Facultate\\_Master\\Advanced Software Engineering Techniques\\Dataset_SpaceNet\\SN7_buildings_train\\train"
    save_directory = "E:\\Facultate\\_Master\\Advanced Software Engineering Techniques\\train_augment"
    patch_size = (128, 128)
    processes_number = 5
    augmentation_number = 1
    work_portion_size = 60 // processes_number
    keep_worker_alive = True
    
    locations = os.listdir(directory)
    locations = locations * augmentation_number
    
    unique_ids = [i + len(os.listdir(save_directory)) for i in range(1, augmentation_number * 60 + 1)]
    
    argument_list = [[directory, save_directory, patch_size, locations[i], unique_ids[i]] for i in range(len(unique_ids))]
    
    result = []
    for i in range(augmentation_number):
        print("Started jobs")
        pool = Pool(processes=processes_number, maxtasksperchild=work_portion_size)
        result = pool.starmap(defs.process_location, argument_list)
        pool.close()
        pool.join()
        
        print(result)
        print("Pool closed")


# In[ ]:




