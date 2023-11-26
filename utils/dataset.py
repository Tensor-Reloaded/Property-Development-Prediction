from __future__ import annotations

import logging
import os
import random
from functools import cache, cached_property

import cv2
import numpy as np
import torch
import torchvision
from omegaconf import OmegaConf
from torch.utils.data import Dataset
from torchvision import transforms

from . import attr_is_valid
from .transformations import init_transforms


def prepare_dataset_and_transforms(dataset_config):
    if dataset_config.name not in datasets:
        logging.error(f"Dataset {dataset_config.name} not implemented!")
        exit()
    cached_transforms, runtime_transforms = None, None
    if hasattr(dataset_config, "transform"):
        cached_transforms, runtime_transforms = init_transforms(dataset_config.transform)

    return dataset_config, cached_transforms, runtime_transforms


def init_dataset(dataset_config, cached_transforms, runtime_transforms, device) -> DatasetWrapper:
    parameters = OmegaConf.to_container(dataset_config.load_params, resolve=True)
    parameters = {k: v for k, v in parameters.items() if v is not None}
    if "device" in parameters and "cuda" in parameters["device"]:
        parameters["device"] = device

    dataset = datasets[dataset_config.name](**parameters, transforms=cached_transforms + runtime_transforms)
    if attr_is_valid(dataset_config, "subset") and dataset_config.subset != '' and dataset_config.subset >= 0:
        dataset = select_dataset_subset(dataset, dataset_config.subset)

    if attr_is_valid(dataset_config, "corrupt") and attr_is_valid(dataset_config, "corrupt_subset"):
        dataset = corrupt_dataset(dataset, dataset_config.corrupt_subset)

    # dataset = DatasetWrapper(dataset=dataset,
    #                          cached_transforms=cached_transforms,
    #                          runtime_transforms=runtime_transforms,
    #                          save_in_memory=dataset_config.save_in_memory)

    return dataset


def select_dataset_subset(dataset, subset: float) -> tuple:
    if subset < 1.0:
        ix_size = int(subset * len(dataset))
    else:
        ix_size = int(subset)
    indices = np.random.choice(len(dataset), size=ix_size, replace=False)

    dataset_subset = []
    for idx in indices:
        dataset_subset.append(dataset[idx])

    return tuple(dataset_subset)


def corrupt_dataset(dataset, corrupt_subset: float) -> tuple:
    if corrupt_subset <= 0.0:
        return dataset
    if corrupt_subset < 1.0:
        corrupt_size = int(corrupt_subset * len(dataset))
    else:
        corrupt_size = int(corrupt_subset)
    dataset = list(dataset)  # mutable sequence
    random.shuffle(dataset)

    new_labels = np.random.randint(0, 10, corrupt_size)
    for i in range(corrupt_subset):
        dataset[i] = dataset[i][0], new_labels[i]

    return tuple(dataset)


def transform_dataset(dataset, transforms: transforms.Compose) -> tuple:
    dataset_copy = []

    for idx in range(len(dataset)):
        tensor, target = dataset[idx]
        tensor, target = transforms((tensor, target))
        dataset_copy.append((tensor, target))

    return tuple(dataset_copy)


class DatasetWrapper(Dataset):
    def __init__(self, dataset, cached_transforms=None, runtime_transforms=None, save_in_memory=False):
        self.dataset = dataset

        if cached_transforms is None:
            cached_transforms = []
        if runtime_transforms is None:
            runtime_transforms = []

        if save_in_memory:
            self.dataset = transform_dataset(self.dataset, transforms.Compose(cached_transforms))
        else:
            runtime_transforms = cached_transforms + runtime_transforms

        self.runtime_transforms = transforms.Compose(runtime_transforms)

    @cache  # Maybe a bit overkill, we should cache the length ourselves.
    def __len__(self):
        return len(self.dataset)

    def __getitem__(self, index):  # This could be @torch.compiled, but check for improvement first
        return self.runtime_transforms(self.dataset[index])


class ComposedDataset(Dataset):
    def __init__(self, dataset_dict):
        # TODO instead of dataset_dict, pass a list of dataset names and read their config files directly
        self.dataset_list = []
        for dataset_name, dataset_params in dataset_dict.items():
            if dataset_name not in datasets:
                print(f"This dataset is not implemented ({dataset_name}), go ahead and commit it")
                exit()

            dataset = datasets[dataset_name](**dataset_params)
            self.dataset_list.append(dataset)

    def __len__(self):
        return max([len(d) for d in self.dataset_list])

    def __getitem__(self, idx):
        tensors = []
        targets = []
        for d in self.dataset_list:
            tensor, target = d[idx % len(d)]
            tensors.append(tensor)
            targets.append(target)
        return tensors, targets

class SpaceNet7Dataset(Dataset):
    def __init__(self, root, transforms=[]):
        self.root_dir = root
        self.transforms = transforms
        self.full_dataset = []
        self.size = 0

        locations = [dir_name for dir_name in os.listdir(self.root_dir) if os.path.isdir(os.path.join(self.root_dir, dir_name))]
        for i, location in enumerate(locations):
            img_files = os.listdir(os.path.join(
                self.root_dir, location, 'images'))
            img_files.sort()
            total_images = len(img_files)
            for index_start in range(total_images):
                for index_end in range(total_images):
                    if index_start == index_end:
                        continue
                    time_skip_1 = (int(img_files[index_start].split('_')[2]) - 2018) * 12 + int(img_files[index_start].split('_')[3]) - 1  # numarul de luni din 2018 ianuarie
                    time_skip_2 = (int(img_files[index_end].split('_')[2]) - 2018) * 12 + int(img_files[index_end].split('_')[3]) - 1  # numarul de luni din 2018 pt target
                    self.full_dataset.append([
                        time_skip_2 - time_skip_1,  # diff de luni
                        os.path.join(self.root_dir, location, 'images', img_files[index_start]), # img input
                        os.path.join(self.root_dir, location, 'images', img_files[index_end]) # img target
                    ])

                    self.size += 1

    def __len__(self):
        return self.size

    def __getitem__(self, idx):
        simple = transforms.Compose([transforms.ToTensor(), transforms.Resize((1024, 1024)), transforms.Normalize([0.3053, 0.4149, 0.4727], [0.1122, 0.1246, 0.1556])])

        data = simple(cv2.imread(self.full_dataset[idx][1], 1))
        target = simple(cv2.imread(self.full_dataset[idx][2], 1))
        
        for transform in self.transforms:
            both_images = torch.cat((data.unsqueeze(0), target.unsqueeze(0)), 0)
            both_images = transform(both_images)
            data = both_images[0]
            target = both_images[1]

        return (self.full_dataset[idx][0], data), target  # timeskip, tensor format images


datasets = {
    'CIFAR-10': torchvision.datasets.CIFAR10,
    'CIFAR-100': torchvision.datasets.CIFAR100,
    'ImageNet2012': torchvision.datasets.ImageFolder,
    'MNIST': torchvision.datasets.MNIST,
    'ComposedDataset': ComposedDataset,
    'SpaceNet7Dataset': SpaceNet7Dataset
}
