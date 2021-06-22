import decouple
import os
import dotenv

from typing import Optional


class __EnvAccessor:
    _dotenv_file: str = dotenv.find_dotenv()

    _log_mode: str = 'LOG_MODE'

    _key_to_encrypt: str = 'KEY_ENCRYPT'
    _algorithm_to_encrypt: str = 'ALGORITHM_ENCRYPT'

    _host_database: str = 'HOST_DATABASE'
    _username_database: str = 'USERNAME_DATABASE'
    _password_database: str = 'PASSWORD_DATABASE'

    _database_user: str = 'DATABASE_USER'
    _database_dictionary: str = 'DATABASE_DICTIONARY'

    _path_yolov3_names: str = 'PATH_YOLOV3_NAMES'
    _path_yolov3_config: str = 'PATH_YOLOV3_CONFIG'
    _path_yolov3_weights: str = 'PATH_YOLOV3_WEIGHTS'

    _path_json_definition_all_word: str = 'PATH_JSON_DEFINITION_ALL_WORD'
    _path_json_definition_common_word: str = 'PATH_JSON_DEFINITION_COMMON_WORD'
    
    _path_json_opted: str = 'PATH_JSON_OPTED'

    _google_credential: str = 'GOOGLE_APPLICATION_CREDENTIALS'

    def __init__(self):
        dotenv.load_dotenv(self._dotenv_file)

    def _set_env(self, key: str, value: str) -> None:
        os.environ[key] = value
        dotenv.set_key(self._dotenv_file, key, value)

    @property
    def log_mode(self) -> str:
        return decouple.config(self._log_mode)

    @log_mode.setter
    def log_mode(self, value: str):
        self._set_env(self._log_mode, value)

    @property
    def key_to_encrypt(self) -> str:
        return decouple.config(self._key_to_encrypt)
    
    @key_to_encrypt.setter
    def key_to_encrypt(self, value: str):
        self._set_env(self._key_to_encrypt, value)

    @property
    def algorithm_to_encrypt(self) -> str:
        return decouple.config(self._algorithm_to_encrypt)

    @algorithm_to_encrypt.setter
    def algorithm_to_encrypt(self, value: str):
        self._set_env(self._algorithm_to_encrypt, value)

    @property
    def host_database(self) -> str:
        return decouple.config(self._host_database)
    
    @host_database.setter
    def host_database(self, value: str):
        self._set_env(self._host_database, value)

    @property
    def username_database(self) -> str:
        return decouple.config(self._username_database)
    
    @username_database.setter
    def username_database(self, value: str):
        self._set_env(self._username_database, value)
    
    @property
    def password_database(self) -> str:
        return decouple.config(self._password_database)

    @password_database.setter
    def password_database(self, value: str):
        self._set_env(self._password_database, value)
    
    @property
    def database_user(self) -> str:
        return decouple.config(self._database_user)

    @database_user.setter
    def database_user(self, value: str):
        self._set_env(self._database_user, value)

    @property
    def database_dictionary(self) -> str:
        return decouple.config(self._database_dictionary)

    @database_dictionary.setter
    def database_dictionary(self, value: str):
        self._set_env(self._database_dictionary, value)

    @property
    def path_yolov3_names(self) -> str:
        return decouple.config(self._path_yolov3_names)

    @path_yolov3_names.setter
    def path_yolov3_names(self, value: str):
        self._set_env(self._path_yolov3_names, value)

    @property
    def path_yolov3_config(self) -> str:
        return decouple.config(self._path_yolov3_config)

    @path_yolov3_config.setter
    def path_yolov3_config(self, value: str):
        self._set_env(self._path_yolov3_config, value)

    @property
    def path_yolov3_weights(self) -> str:
        return decouple.config(self._path_yolov3_weights)

    @path_yolov3_weights.setter
    def path_yolov3_weights(self, value: str):
        self._set_env(self._path_yolov3_weights, value)

    @property
    def path_json_opted(self) -> str:
        return decouple.config(self._path_json_opted)

    @path_json_opted.setter
    def path_json_opted(self, value: str):
        self._set_env(self._path_json_opted, value)

    @property
    def path_json_definition_all_word(self) -> str:
        return decouple.config(self._path_json_definition_all_word)

    @path_json_definition_all_word.setter
    def path_json_definition_all_word(self, value: str):
        self._set_env(self._path_json_definition_all_word, value)

    @property
    def path_json_definition_common_word(self) -> str:
        return decouple.config(self._path_json_definition_common_word)

    @path_json_definition_common_word.setter
    def path_json_definition_common_word(self, value: str):
        self._set_env(self._path_json_definition_common_word, value)

    @property
    def google_credential(self) -> str:
        return decouple.config(self._google_credential)

    @google_credential.setter
    def google_credential(self, value: str):
        self._set_env(self._google_credential, value)


env_accessor = __EnvAccessor()
