import cv2
from PIL import Image

import numpy as np

import base64

import io


ImageBase64 = str


# Take in base64 string and return PIL image
def string_to_image(image_base64: ImageBase64) -> Image:
    image_data = base64.b64decode(image_base64)
    return Image.open(io.BytesIO(image_data))


# convert PIL Image to an RGB image( technically a numpy array ) that's compatible with opencv
def to_rgb(image: Image):
    return cv2.cvtColor(np.array(image), cv2.COLOR_BGR2RGB)
