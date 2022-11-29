#!/usr/bin/env python
# coding: utf-8

# In[3]:


from mpire import WorkerPool
from defs import process_location
import os

if __name__ == '__main__':
    directory = "E:\\Facultate\\_Master\\Advanced Software Engineering Techniques\\Dataset_SpaceNet\\SN7_buildings_train\\train"
    save_directory = "E:\\Facultate\\_Master\\Advanced Software Engineering Techniques\\train_augment"
    patch_size = (128, 128)
    processes_number = 5
    augmentation_number = 1
    worker_work_portion = 14
    work_portion_size = 1
    keep_worker_alive = True
    
    locations = os.listdir(directory)
    argument_list = [[directory, save_directory, patch_size, locations[i]] for i in range(len(locations))]
    
    for i in range(augmentation_number):
        with WorkerPool(n_jobs=processes_number, keep_alive=keep_worker_alive) as pool:
            result = pool.map_unordered(process_location, argument_list, worker_lifespan=worker_work_portion, chunk_size=work_portion_size)


# In[ ]:




