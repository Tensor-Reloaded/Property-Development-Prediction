import torch
import torch.functional as F
import torchvision.transforms as transforms
from torch import nn
import numpy as np
import os

class Encoder(nn.Module):
    def __init__(self, encoded_space_dim, fc2_input_dim):
        super().__init__()
        
        self.encoder_cnn = nn.Sequential(
            nn.Conv2d(3, 8, 3, stride=2, padding=1),
            nn.ReLU(True),
            nn.Conv2d(8, 16, 3, stride=2, padding=1),
            nn.BatchNorm2d(16),
            nn.ReLU(True),
            nn.Conv2d(16, 32, 3, stride=2, padding=0),
            nn.ReLU(True)
        )
        self.layers_cnn = []
        self.layers_cnn.append(nn.Conv2d(3, 8, 3, stride=2, padding=1))
        self.layers_cnn.append(nn.ReLU(True))
        self.layers_cnn.append(nn.Conv2d(8, 16, 3, stride=2, padding=1))
        self.layers_cnn.append(nn.BatchNorm2d(16))
        self.layers_cnn.append(nn.ReLU(True))
        self.layers_cnn.append(nn.Conv2d(16, 32, 3, stride=2, padding=0))
        self.layers_cnn.append(nn.ReLU(True))
        
        self.flatten = nn.Flatten(start_dim=1)
        
        self.layers_lin = []
        self.layers_lin.append(nn.Linear(3 * 3 * 32, 128))
        self.layers_lin.append(nn.ReLU(True))
        self.layers_lin.append(nn.Linear(128, encoded_space_dim))
        
        self.encoder_lin = nn.Sequential(
            nn.Linear(3 * 3 * 32, 128),
            nn.ReLU(True),
            nn.Linear(128, encoded_space_dim)
        )
        
    def forward(self, x):
#         print("------------------------------------\nEncoding\n------------------------------------\n")
#         print(f"Initial shape: {x.shape}")
        for i in range(0, len(self.layers_cnn)):
            x = self.layers_cnn[i](x)
            #print(f"{i} : {x.shape}")
        #x = self.encoder_cnn(x)
        x = self.flatten(x)
        #print(f"After flatten: {x.shape}")
        #x = self.encoder_lin(x)
        for i in range(0, len(self.layers_lin)):
            x = self.layers_lin[i](x)
            #print(f"{i} : {x.shape}")
        return x
    
class Decoder(nn.Module):
    def __init__(self, encoded_space_dim,fc2_input_dim):
        super().__init__()
        self.layers_cnn = []
        self.layers_cnn.append(nn.ConvTranspose2d(32, 16, 3, stride=2, output_padding=1))
        self.layers_cnn.append(nn.BatchNorm2d(16))
        self.layers_cnn.append(nn.ReLU(True))
        self.layers_cnn.append(nn.ConvTranspose2d(16, 8, 3, stride=2, padding=1, output_padding=1))
        self.layers_cnn.append(nn.BatchNorm2d(8))
        self.layers_cnn.append(nn.ReLU(True))
        self.layers_cnn.append(nn.ConvTranspose2d(8, 3, 3, stride=2, padding=1, output_padding=1))

        
        self.layers_lin = []
        self.layers_lin.append(nn.Linear(encoded_space_dim, 128))
        self.layers_lin.append(nn.ReLU(True))
        self.layers_lin.append(nn.Linear(128, 3 * 3 * 32))
        self.layers_lin.append(nn.ReLU(True))
        
        self.decoder_lin = nn.Sequential(
            nn.Linear(encoded_space_dim, 128),
            nn.ReLU(True),
            nn.Linear(128, 3 * 3 * 32),
            nn.ReLU(True)
        )

        self.unflatten = nn.Unflatten(dim=1, unflattened_size=(32, 3, 3))

        self.decoder_conv = nn.Sequential(
            nn.ConvTranspose2d(32, 16, 3, stride=2, output_padding=0),
            nn.BatchNorm2d(16),
            nn.ReLU(True),
            nn.ConvTranspose2d(16, 8, 3, stride=2, padding=1, output_padding=1),
            nn.BatchNorm2d(8),
            nn.ReLU(True),
            nn.ConvTranspose2d(8, 3, 3, stride=2, padding=1, output_padding=1)
        )
        
    def forward(self, x):
#         print("\n------------------------------------\nDecoding\n------------------------------------\n")
#         print(f"Initial shape: {x.shape}")
        for i in range(0, len(self.layers_lin)):
            x = self.layers_lin[i](x)
            #print(f"{i} : {x.shape}")
        
        # x = self.decoder_lin(x)
        x = self.unflatten(x)
        #print(f"Shape after unflatten: {x.shape}")
        
        for i in range(0, len(self.layers_cnn)):
            x = self.layers_cnn[i](x)
            #print(f"{i} : {x.shape}")
        # x = self.decoder_conv(x)
        x = torch.sigmoid(x)
        return x


class AE(nn.Module):
    def __init__(self):
        super(AE, self).__init__()
        self.temporal_embedding = nn.Parameter(torch.rand(1, 3, 32, 32))
        self.encoder = Encoder(32*32*3, 128)
        self.decoder = Decoder(32*32*3, 128)

    def forward(self, x, time_skip):
        time_skip_vector = torch.einsum('i, iabc->iabc', time_skip, self.temporal_embedding.repeat(time_skip.size(0), 1, 1, 1))
        x = x + time_skip_vector
        x = self.encoder(x)
        # x = x + time_skip_vector
        x = self.decoder(x)
        return x
