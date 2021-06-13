import cv2
from PIL import Image

import numpy as np

import base64

import decouple

import io
import time


ImageBase64 = str


# Take in base64 string and return PIL image
def stringToImage(image_base64: ImageBase64) -> Image:
    image_data = base64.b64decode(image_base64)
    return Image.open(io.BytesIO(image_data))


# convert PIL Image to an RGB image( technically a numpy array ) that's compatible with opencv
def toRGB(image: Image):
    return cv2.cvtColor(np.array(image), cv2.COLOR_BGR2RGB)


def label_image(image, min_confidence: float = 0.5):
    # load the COCO class labels our YOLO model was trained on
    labels_path = decouple.config("yolov3_names")
    labels = open(labels_path).read().strip().split("\n")

    # derive the paths to the YOLO weights and model configuration
    weights_path = decouple.config("yolov3_weights")
    config_path = decouple.config("yolov3_config")

    # load our YOLO object detector trained on COCO dataset (80 classes)
    print("[INFO] loading YOLO from disk...")
    net = cv2.dnn.readNetFromDarknet(config_path, weights_path)

    # grab image's spatial dimensions
    (height, width) = image.shape[:2]

    # determine only the *output* layer names that we need from YOLO
    ln = net.getLayerNames()
    ln = [ln[i[0] - 1] for i in net.getUnconnectedOutLayers()]

    # construct a blob from the input image and then perform a forward pass of the YOLO object detector,
    # giving us our bounding boxes and associated probabilities
    blob = cv2.dnn.blobFromImage(image, 1 / 255.0, (416, 416),
                                 swapRB=True, crop=False)
    net.setInput(blob)
    start = time.time()
    layer_outputs = net.forward(ln)
    end = time.time()
    # show timing information on YOLO
    print("[INFO] YOLO took {:.6f} seconds".format(end - start))

    max_confidence = min_confidence
    argmax_class_id = None

    # loop over each of the layer outputs
    for output in layer_outputs:
        # loop over each of the detections
        for detection in output:
            # extract the class ID and confidence (i.e., probability) of the current object detection
            scores = detection[5:]
            class_id = np.argmax(scores)
            confidence = scores[class_id]

            # filter out weak predictions by ensuring the detected probability is greater than the minimum probability
            if confidence > min_confidence:
                max_confidence = confidence
                argmax_class_id = class_id

    return labels[argmax_class_id] if argmax_class_id is not None else None
