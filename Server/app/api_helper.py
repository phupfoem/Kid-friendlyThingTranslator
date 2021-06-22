import decouple

from yolov3.yolo import ImageLabeler
from yolov3.image_converter import string_to_image, to_rgb

from word_definition.memory_dictionary import MemoryDictionary
from word_definition.database_dictionary import DatabaseDictionary

from env_access import env_accessor

from app.model import *

from fastapi import Body

from google.cloud import vision
from google.auth.exceptions import DefaultCredentialsError

import mysql.connector

import cv2

import logging
import time

from enum import Enum


class ApiHelper:
    class DbId(Enum):
        User = env_accessor.database_user
        Dictionary = env_accessor.database_dictionary

    def __init__(self):
        self._create_conns()

        self._create_google_cloud_client()
        self._create_yolov3_image_labeler()

        self._create_memory_dictionary()
        self._create_database_dictionary()

    def _create_conns(self):
        self._db_conns = {}
        for db_id in self.DbId:
            db_name = db_id.value

            is_success = False
            while not is_success:
                try:
                    logging.info("Connecting to %s database...", db_name)

                    self._db_conns[db_id] = mysql.connector.connect(
                        host=env_accessor.host_database,
                        user=env_accessor.username_database,
                        password=env_accessor.password_database,
                        database=db_name
                    )

                    logging.info("Connected to %s database", db_name)

                    is_success = True
                except mysql.connector.Error as err:
                    logging.error("Failed to connect to %s database: %s", db_name, str(err))

                    time_to_retry = 5
                    logging.info("Retry connecting in %d seconds", time_to_retry)
                    time.sleep(time_to_retry)

    def _create_google_cloud_client(self):
        logging.info("Instantiating a Google Cloud Vision Client...")

        try:
            logging.info("Instantiating a Google Cloud Vision Client by default credentials...")

            self._gg_cloud_vision_client = vision.ImageAnnotatorClient()
        except DefaultCredentialsError as err:
            logging.warning("Failed to instantiate a Google Cloud Vision Client by default credentials: %s", str(err))

            try:
                logging.info("Instantiating a Google Cloud Vision Client by environment variable...")

                self._gg_cloud_vision_client = vision.ImageAnnotatorClient.from_service_account_json(
                    env_accessor.google_credential
                )
            except (decouple.UndefinedValueError, FileNotFoundError) as err:
                logging.warning("Failed to instantiate a Google Cloud Vision Client: %s", str(err))

                self._gg_cloud_vision_client = None
                return

        logging.info("Google Cloud Vision Client instantiated")

    def _create_yolov3_image_labeler(self):
        try:
            logging.info("Loading YOLO from disk...")

            self._yolov3_image_labeler = ImageLabeler(
                labels_path=env_accessor.path_yolov3_names,
                config_path=env_accessor.path_yolov3_config,
                weights_path=env_accessor.path_yolov3_weights
            )

            logging.info("Loaded YOLO")
        except decouple.UndefinedValueError as err:
            logging.warning("Failed to load YOLO")

            self._yolov3_image_labeler = None

    def _create_memory_dictionary(self):
        try:
            logging.info("Loading a Memory Dictionary...")

            self._memory_word_dictionary = MemoryDictionary(env_accessor.path_json_definition_common_word)

            logging.info("Loaded a Memory Dictionary")
        except MemoryError as err:
            logging.warning("Failed to load a Memory Dictionary: %s", str(err))

            self._memory_word_dictionary = None

    def _create_database_dictionary(self):
        try:
            logging.info("Loading a Database Dictionary...")

            self._database_word_dictionary = DatabaseDictionary(self._db_conns[self.DbId.Dictionary])

            logging.info("Loaded a Database Dictionary")
        except Exception as e:
            logging.error("Failed to load a Database Dictionary: %s", str(e))

    def is_user_login_info_exist(self, data: UserLoginSchema) -> bool:
        cursor = self._db_conns[self.DbId.User].cursor()

        sql = "SELECT email FROM Accounts WHERE email = %s AND passphrase = %s"
        param = (data.email, data.password)

        cursor.execute(sql, param)

        return cursor.fetchone() is not None

    def get_user_name(self, email: str) -> str:
        cursor = self._db_conns[self.DbId.User].cursor()

        sql = "SELECT name FROM Accounts WHERE email = %s"
        param = (email,)

        cursor.execute(sql, param)

        res = cursor.fetchone()

        return res[0] if res is not None else ""

    def create_user(self, user: UserSignupSchema = Body(...)) -> bool:
        conn = self._db_conns[self.DbId.User]
        cursor = conn.cursor()

        sql = "INSERT INTO Accounts(email, passphrase, name) VALUES (%s, %s, %s)"
        param = (user.email, user.password, user.name)

        try:
            cursor.execute(sql, param)
            conn.commit()

            return True
        except mysql.connector.errors.IntegrityError:
            return False

    def label_image_by_gg_cloud_vision(self, image_base64: str) -> str:
        if self._gg_cloud_vision_client is None:
            return ""

        image = to_rgb(string_to_image(image_base64))
        _, encoded_image = cv2.imencode('.jpg', image)
        image = vision.Image(content=encoded_image.tobytes())

        # Try using GG Cloud service
        try:
            # Performs label detection on the image file
            response = self._gg_cloud_vision_client.label_detection(image=image)
            labels = response.label_annotations

            if response.error.message:
                return ""

            return labels[0].description if labels else ""
        except Exception as err:
            logging.warning("Failed to use Google Cloud Vision: %s", str(err))

    def label_image_by_yolov3(self, image_base64: str) -> str:
        if self._yolov3_image_labeler is None:
            return ""

        image = to_rgb(string_to_image(image_base64))

        # Performs label detection on the image file
        return self._yolov3_image_labeler.label_image(image)

    def define_word_by_memory_dictionary(self, word: str) -> str:
        return self._memory_word_dictionary.define(word)

    def define_word_by_database_dictionary(self, word: str) -> str:
        return self._database_word_dictionary.define(word)

    def __del__(self):
        for conn in self._db_conns.values():
            conn.close()
