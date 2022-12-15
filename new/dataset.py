import torch
import numpy as np
import os
import random
import cv2
import torchvision.transforms as transforms

class Spacenet7Dataset(torch.utils.data.Dataset):
    def __init__(self, root_dir, transform=None):
        self.root_dir = root_dir
        self.transform = transform
        
        self.dataset = {}
        self.size = 0
        locations = [dir_name for dir_name in os.listdir(self.root_dir) if os.path.isdir(os.path.join(self.root_dir, dir_name))]
        for i,location in enumerate(locations):
            self.dataset[i] = {(int(img_file.split('_')[2])-2018)*12+int(img_file.split('_')[3])-1: (location,img_file) for img_file in os.listdir(os.path.join(self.root_dir, location,'images'))}
            self.size += len(self.dataset[i]) - 1
        
    def __len__(self):
        return self.size

    def __getitem__(self, idx):
        location_id = 0
        while True:
            if idx < len(self.dataset[location_id]) - 1:
                break
            idx -= len(self.dataset[location_id])
            location_id += 1
            
        target_idx = random.randint(idx+1, len(self.dataset[location_id])-1)
        
        idx = list(self.dataset[location_id].keys())[idx]
        target_idx = list(self.dataset[location_id].keys())[target_idx]
        time_skip = idx - target_idx
        
        data = cv2.imread(os.path.join(self.root_dir, self.dataset[location_id][idx][0],'images',self.dataset[location_id][idx][1]), 1)
        target = cv2.imread(os.path.join(self.root_dir, self.dataset[location_id][idx][0],'images',self.dataset[location_id][target_idx][1]), 1)
        
        if self.transform:
            data = self.transform(data)
            target = self.transform(target)

        return data,target,time_skip
    
if __name__ == '__main__':
    dataset = Spacenet7Dataset('E:/Facultate/_Master/Advanced Software Engineering Techniques/train_augment')