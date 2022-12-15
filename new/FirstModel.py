#!/usr/bin/env python
# coding: utf-8

# In[1]:


import torch
import torch.nn.functional as F
import torchvision.transforms as transforms
from torchmetrics.functional import structural_similarity_index_measure as structural_similarity
from torch import nn
import numpy as np
import os

from model import AE
from dataset import Spacenet7Dataset


# In[2]:


if __name__ == '__main__':
    epochs = 100
    batch_size = 64
    lr = 1e-5
    weight_decay = 1e-5
    
    device = 'cpu'#'cuda' if torch.cuda.is_available() else 'cpu'
    model = AE().to(device)
    
    transform = transforms.Compose([
        transforms.ToTensor(),
        # transform.normalize((0.5,0.5,0.5), (0.5,0.5,0.5)), # TODO: Calculate correct mean and std per channel
        transforms.RandomHorizontalFlip(),
        transforms.RandomVerticalFlip(),
        transforms.RandomRotation(180),
        transforms.RandomCrop(32),
    ])
        
    dataset = Spacenet7Dataset('E:/Facultate/_Master/Advanced Software Engineering Techniques/train_augment', transform=transform)
    train_loader = torch.utils.data.DataLoader(dataset, batch_size=batch_size, shuffle=True) #, num_workers=8, pin_memory=True)
    optimizer = torch.optim.AdamW(model.parameters(), lr=lr, weight_decay=weight_decay)
    for epoch in range(epochs):
        mse_losses = []
        cosine_similarity_losses = []
        structural_similarity_losses = []
        
        total_losses = []
        for batch_idx, (data, target, time_skip) in enumerate(train_loader):
            data = data.to(device)
            target = target.to(device)
            time_skip = time_skip.to(device)
            
            output = model(data, time_skip)
            #print(f'Output: {output.shape}\nTarget: {target.shape}')
            
            mse_loss = F.mse_loss(output, target)
            cosine_similarity_loss = F.cosine_similarity(output, target)
            structural_similarity_loss = structural_similarity(output, target)
            loss = (mse_loss + cosine_similarity_loss + structural_similarity_loss) / 3
            
            loss.mean().backward()
            optimizer.step()
            optimizer.zero_grad()
            
            mse_losses.append(mse_loss.mean().item())
            cosine_similarity_losses.append(cosine_similarity_loss.mean().item())
            structural_similarity_losses.append(structural_similarity_loss.mean().item())
            total_losses.append(loss.mean().item())
            print(f'Batch {batch_idx}/{len(train_loader)}: MSE loss: {mse_loss.mean().item()}, Cosine similarity loss: {cosine_similarity_loss.mean().item()}, Structural similarity loss: {structural_similarity_loss.mean().item()}, Total loss: {loss.mean().item()}')
        print(f'Epoch {epoch}/{epochs}: MSE loss: {np.mean(mse_loss)}, Cosine similarity loss: {np.mean(cosine_similarity_loss)}, Structural similarity loss: {np.mean(structural_similarity_loss)}, Total loss: {np.mean(loss)}')

