# Kid-friendly Thing Translator Server

## Prerequisite
### Basic server features (signup, login)
To be able to deploy, you should have:
1. Python 3 installed on your computer.
1. MySQL installed on your computer.

### Image Labelling feature
To be able to deploy, you should have at least one of these:
+ Google Cloud credentials.
+ YOLOv3 configuration files.

### Dictionary:
To be able to deploy, you should have:
+ JSON files containing a dictionary mapping words to definition.

## Installation Guide
To ease the hassle, here are some guides to satisfy these prerequisites:
+ You can download the newest Python 3 installer at [Python downloads](https://www.python.org/downloads/).
+ You can download a free MySQL installer at [MySQL downloads](https://www.mysql.com/downloads/).
+ You can download Google Cloud credentials by following the instructions at [Getting started with authentication](https://cloud.google.com/docs/authentication/getting-started#cloud-console).
+ You can download free YOLOv3 configuration files from Darknet project (but no need to install) at [Darknet project](https://pjreddie.com/darknet/yolo/) by following steps outlined in section *Detection Using A Pre-Trained Model*.
+ You can download a free JSON version of The Online Text Plain English Dictionary (OPTED) at [OPTED JSON repo](https://github.com/eddydn/DictionaryDatabase) under MIT License.


## Deployment Guide
### Initialize
1. Fork/Clone repo.

1. Create and activate a virtual environment:
   ```sh
   python3 -m venv venv && source venv/bin/activate
   ```
   or just use an IDE like pycharm to automate venv creation and activation

   or just simply use your global interpreter.

1. Install the requirements:
   ```sh
   pip install -r requirements.txt
   ```
   depending on your configurations, however, you may have to manually install these using pip or your IDE.

1. Create/Modify the Environment Variables file (.env) to set these variables:
   + **KEY_ENCRYPT** and **ALGORITHM_ENCRYPT** are used to encode and decode JWT and should not be exposed in any way.
   + **HOST_DATABASE**, **USERNAME_DATABASE** and **PASSWORD_DATABASE** are used to create connection to MySQL.
   + **DATABASE_USER** and **DATABASE_DICTIONARY** are the names of databases, where:
      + **DATABASE_USER** contains data on users.
      + **DATABASE_DICTIONARY** contains word dictionary.
      
      These are separate from code in case your system has existing databases with the same names.
   + **PATH_JSON_DEFINITION_ALL_WORD** and **PATH_JSON_DEFINITION_COMMON_WORD** are paths to JSON dictionary files in format specified above.
      + If you decide to use OPTED JSON, then do not set these. Instead, set **PATH_JSON_OPTED** to the path to the OPTED JSON. 
   + **PATH_YOLOV3_NAMES**, **PATH_YOLOV3_CONFIG** and **PATH_YOLOV3_WEIGHTS** are paths to the YOLOv3 configuration files.
      + If you decide to use Darknet project, you can find path to:
         + **PATH_YOLOV3_NAMES** by searching _coco.names_ in the Darknet repo.
         + **PATH_YOLOV3_CONFIG** by searching _yolov3.cfg_ in the Darknet repo.
         + **PATH_YOLOV3_WEIGHTS** is the heavy pre-trained weight file downloaded separately from the repo.
   + **GOOGLE_APPLICATION_CREDENTIALS** is the path to the Google Cloud Credentials JSON file. You can set this variable in _.env_, or follow Google's instructions and set directly on command line.
   
   See [.env](.env) for an example.

1. Run the init code:
   ```sh
   python init.py
   ```

### Run the server
1. Run the app:
   ```sh
   python main.py
   ```
   or
   ```sh
   uvicorn app.api:app --host 0.0.0.0 --port 8000
   ```

1. Test at [http://localhost:8000](http://localhost:8000)
   
   or through the client app.

## Reference
To learn more on the implementation of authentication by JWT under MIT License, check out [this post](https://testdriven.io/blog/fastapi-jwt-auth/).
