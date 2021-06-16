import cv2

import numpy as np


class ImageLabeler:
    def __init__(self, labels_path: str, weights_path: str, config_path: str):
        self._labels = open(labels_path).read().strip().split("\n")

        # load our YOLO object detector trained on COCO dataset (80 classes)
        self._net = cv2.dnn.readNetFromDarknet(config_path, weights_path)

    def label_image(self, image, min_confidence: float = 0.5) -> str:
        # grab image's spatial dimensions
        (height, width) = image.shape[:2]

        # determine only the *output* layer names that we need from YOLO
        ln = self._net.getLayerNames()
        ln = [ln[i[0] - 1] for i in self._net.getUnconnectedOutLayers()]

        # construct a blob from the input image and then perform a forward pass of the YOLO object detector,
        # giving us our bounding boxes and associated probabilities
        blob = cv2.dnn.blobFromImage(image, 1 / 255.0, (416, 416),
                                     swapRB=True, crop=False)
        self._net.setInput(blob)
        layer_outputs = self._net.forward(ln)

        argmax_class_id = None

        # loop over each of the layer outputs
        for output in layer_outputs:
            # loop over each of the detections
            for detection in output:
                # extract the class ID and confidence (i.e., probability) of the current object detection
                scores = detection[5:]
                class_id = np.argmax(scores)
                confidence = scores[class_id]

                # filter out weak predictions
                if confidence > min_confidence:
                    argmax_class_id = class_id

        return self._labels[argmax_class_id] if argmax_class_id is not None else ""
