import base64

import numpy as np
import torch
import torchvision.transforms as transforms
from PIL import Image, ImageFile
import sys
from io import BytesIO

from model import AE

ImageFile.LOAD_TRUNCATED_IMAGES = True

device = 'cpu'

model = AE().to(device)
model.load_state_dict(torch.load(
    r'G:\Master\ASET\Property-Development-Prediction\work\lab5\PDP_api\src\main\resources\Model\model_weightsM_20.pth',
    map_location=torch.device('cpu')))
model.eval()

file_image_path = sys.argv[1]
skip_time = int(sys.argv[2])

with open(file_image_path, "r") as f:
    inputs = f.read()
    image = Image.open(BytesIO(base64.b64decode(inputs)))
    image.LOAD_TRUNCATED_IMAGES = True

image = image.resize((576, 576))

patch_size = 32
width_index = image.width // patch_size
height_index = image.height // patch_size
new_image = Image.new(mode="RGB", size=image.size)

for index_w in range(width_index + 1):
    for index_h in range(height_index + 1):
        cut_point_x = index_w * patch_size
        cut_point_y = index_h * patch_size
        image_crop = image.crop((cut_point_x,
                                 cut_point_y,
                                 cut_point_x + patch_size,
                                 cut_point_y + patch_size))
        image_crop = np.array(Image.fromarray(np.array(image_crop), 'RGB'))

        image_crop = torch.Tensor(image_crop.reshape(1, 3, 32, 32))
        transform = transforms.Normalize((0.5, 0.5, 0.5), (0.5, 0.5, 0.5))
        image_crop = transform(image_crop)

        time_skip = torch.Tensor([skip_time]).to(device)
        image_crop = image_crop.to(device)

        with torch.no_grad():
            pred = model(image_crop, time_skip)

        pred = pred.detach().cpu().numpy().reshape((32, 32, 3, 1)).squeeze()
        img = Image.fromarray(pred, 'RGB')
        new_image.paste(img, (cut_point_x, cut_point_y))

buffered = BytesIO()
new_image.save(buffered, format="JPEG")
image_bytes = base64.b64encode(buffered.getvalue())
image_string = image_bytes.decode('ascii')
print(image_string)
