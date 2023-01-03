import torch
import numpy as np
import os
import random
import cv2
import torchvision.transforms as transforms
import copy
import random

class Spacenet7Dataset(torch.utils.data.Dataset):
    def __init__(self, root_dir, transform=None):
        self.root_dir = root_dir
        self.transform = transform
        self.full_dataset = []
        self.size = 0
        self.season = [-1, 0, 1, 1, 1, 2, 2, 2, 3, 3, 3, 0, 0]
        
        locations = [dir_name for dir_name in os.listdir(self.root_dir) if os.path.isdir(os.path.join(self.root_dir, dir_name))]
        for i, location in enumerate(locations):
            img_files = os.listdir(os.path.join(self.root_dir, location,'images'))
            img_files.sort()
            total_images = len(img_files)
            for index_start in range(total_images):
                for index_end in range(index_start + 1, total_images):
                    time_skip_origin = (int(img_files[index_start].split('_')[2]) - 2018)*12+int(img_files[index_start].split('_')[3]) - 1
                    time_skip_origin_1 = (int(img_files[index_end].split('_')[2]) - 2018)*12+int(img_files[index_end].split('_')[3]) - 1
                    self.full_dataset.append([time_skip_origin_1 - time_skip_origin,
                                              self.season[int(img_files[index_start].split('_')[3])],
                                          os.path.join(self.root_dir, location, 'images', img_files[index_start]),
                                          os.path.join(self.root_dir, location, 'images', img_files[index_end])])
                
                    self.size += 1
        
    def __len__(self):
        return self.size

    def __getitem__(self, idx):
        simple = transforms.ToTensor()
        
        data = simple(cv2.imread(self.full_dataset[idx][2], 1))
        target = simple(cv2.imread(self.full_dataset[idx][3], 1))
        if self.transform:
            both_images = torch.cat((data.unsqueeze(0), target.unsqueeze(0)), 0)
            both_images = self.transform(both_images)
            data = both_images[0]
            target = both_images[1]
            
#        if self.apply_transforms:
#             simple_transforms = transforms.Compose([
#                 transforms.ToTensor(),
#                 transforms.Normalize((0.5,0.5,0.5), (0.5,0.5,0.5))
#             ])
#             horizontal_flip = random.random()
#             vertical_flip = random.random()
#             angle = transforms.RandomRotation(180).get_params()
#             i, j, h, w = transforms.RandomCrop.get_params(data, output_size=(32, 32))
            
#             data = simple_transforms(data)
#             target = simple_transforms(target)
            
#             if random.random() > 0.5:
#                 data = transforms.hflip(data)
#                 target = transforms.hflip(target)
            
#             if random.random() > 0.5:
#                 data = transforms.vflip(data)
#                 target = transforms.vflip(target)
            
#             data = transforms.crop(data, i, j, h, w)
#             target = transforms.crop(target, i, j, h, w)

        return self.full_dataset[idx][0], self.full_dataset[idx][1], data, target 
    
if __name__ == '__main__':
    dataset = Spacenet7Dataset('E:/Facultate/_Master/Advanced Software Engineering Techniques/train_augment')