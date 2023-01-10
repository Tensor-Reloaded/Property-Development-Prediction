from flask import Flask, request
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


app = Flask(__name__)
app.config["DEBUG"] = True

@app.route('/api/predictProperty',methods=["GET","POST"])
def predictAPI():
    content = request.get_json()
    print(content)
    return predictImage(content['image'],content['yearsInFuture'])

def predictImage(imageString, yearsInFuture):
    device = 'cpu'
    model = AE().to(device)
    model.load_state_dict(torch.load(
        r'M7.pth',
        map_location=torch.device('cpu')))
    model.eval()

    image = Image.open(BytesIO(base64.b64decode(imageString)))
    image = image.resize((576, 576))

    patch_size = 32
    width_index = image.width // patch_size
    height_index = image.height // patch_size
    new_image = Image.new(mode="RGB", size=image.size)
    
    seasons = np.array([-1, 0, 1, 1, 1, 2, 2, 2, 3, 3, 3, 0, 0])
    current_season = seasons[(int(str(datetime.date.today())[5:7]) + time_skip_index) % 12 + 1]
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
            transform = transforms.Normalize((78.1719, 106.2079, 121.0767), (44.5890, 48.2923, 60.3073))
            image_crop = transform(image_crop)

            time_skip = torch.Tensor([time_skip_index]).to(device)
            image_crop = image_crop.to(device)
            season = torch.Tensor([current_season]).to(device)

            with torch.no_grad():
                pred = model(image_crop, time_skip, season)

            img = transforms.ToPILImage()(pred.squeeze(0))
            new_image.paste(img, (cut_point_x, cut_point_y))

    buffered = BytesIO()
    new_image.save(buffered, format="JPEG")
    image_bytes = base64.b64encode(buffered.getvalue())
    image_string_result = image_bytes.decode('ascii')

    return {
        "image":image_string_result,
        "predictedPrice":-1.0
    }

app.run()