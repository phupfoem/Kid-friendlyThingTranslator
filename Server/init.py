from env_access import env_accessor

import mysql.connector

import json

import decouple
import logging

import pathlib
import os

from typing import List


logging.basicConfig(level=logging.DEBUG)


class ServerInitializer:
    def run(self):
        self.check_existence()

        self.create_databases()
        self.create_tables_in_user_database()
        self.create_tables_in_dictionary_database()

        try:
            _ = env_accessor.path_json_definition_all_word
        except decouple.UndefinedValueError:
            self.parse_OPTED_json(env_accessor.path_json_opted)

        self.populate_definition_table()

        try:
            _ = env_accessor.path_json_definition_common_word
        except decouple.UndefinedValueError:
            self.create_definition_common_word_json()

    @classmethod
    def check_existence(cls) -> None:
        _ = env_accessor.host_database
        _ = env_accessor.username_database
        _ = env_accessor.password_database

        _ = env_accessor.database_user
        _ = env_accessor.database_dictionary

        try:
            _ = env_accessor.google_credential
        except decouple.UndefinedValueError as err:
            try:
                _ = env_accessor.path_yolov3_names
                _ = env_accessor.path_yolov3_config
                _ = env_accessor.path_yolov3_weights
            except decouple.UndefinedValueError:
                raise err

        try:
            _ = env_accessor.path_json_definition_all_word
            _ = env_accessor.path_json_definition_common_word
        except decouple.UndefinedValueError as err:
            try:
                _ = env_accessor.path_json_opted
            except decouple.UndefinedValueError:
                raise err

    @classmethod
    def create_databases(cls):
        # Connect to MySQL
        db_conn = mysql.connector.connect(
            host=env_accessor.host_database,
            user=env_accessor.username_database,
            password=env_accessor.password_database
        )
        cursor = db_conn.cursor()

        try:
            # Create db User and Dictionary
            for db_name in [env_accessor.database_user, env_accessor.database_dictionary]:
                sql = """create database if not exists {}""".format(db_name)
                cursor.execute(sql)
        except (mysql.connector.errors.OperationalError, mysql.connector.errors.ProgrammingError) as e:
            logging.warning("MySQLError during execute statement \n\tArgs: %s", str(e.args))
        finally:
            cursor.close()
            db_conn.close()

    @classmethod
    def create_tables_in_user_database(cls):
        # Connect to User
        db_conn = mysql.connector.connect(
            host=env_accessor.host_database,
            user=env_accessor.username_database,
            password=env_accessor.password_database,
            database=env_accessor.database_user
        )
        cursor = db_conn.cursor()

        try:
            # Create in User, table Accounts
            sql = """create table if not exists Accounts(
                            email varchar(128) not null,
                            passphrase varchar(128) not null,
                            name varchar(128) not null,
                            constraint Accounts_pk primary key (email)
                        )"""
            cursor.execute(sql)
        except (mysql.connector.errors.OperationalError, mysql.connector.errors.ProgrammingError) as e:
            logging.warning("MySQLError during execute statement \n\tArgs: %s", str(e.args))
        finally:
            cursor.close()
            db_conn.close()

    @classmethod
    def create_tables_in_dictionary_database(cls):
        # Connect to Dictionary
        db_conn = mysql.connector.connect(
            host=env_accessor.host_database,
            user=env_accessor.username_database,
            password=env_accessor.password_database,
            database=env_accessor.database_dictionary
        )
        cursor = db_conn.cursor()

        try:
            # Create in Dictionary, table Definition
            sql = """create table if not exists Definition(
                            word varchar(100) not null,
                            description varchar(1000) not null,
                            constraint Definition_pk primary key (word)
                        )"""
            cursor.execute(sql)
        except (mysql.connector.errors.OperationalError, mysql.connector.errors.ProgrammingError) as e:
            logging.warning("MySQLError during execute statement \n\tArgs: %s", str(e.args))
        finally:
            cursor.close()
            db_conn.close()

    @classmethod
    def populate_definition_table(cls):
        # Connect to Dictionary
        db_conn = mysql.connector.connect(
            host=env_accessor.host_database,
            user=env_accessor.username_database,
            password=env_accessor.password_database,
            database=env_accessor.database_dictionary
        )
        cursor = db_conn.cursor()

        step = 1000
        try:
            # Populate Definition
            sql = """insert into Definition (word, description) values (%s, %s)"""

            with open(env_accessor.path_json_definition_all_word) as all_word_json:
                params = list(json.load(all_word_json).items())

            cls._executemany_in_decreasing_step(cursor, sql, params, step)
            db_conn.commit()
        except (mysql.connector.errors.OperationalError, mysql.connector.errors.ProgrammingError) as e:
            logging.warning("MySQLError during execute statement \n\tArgs: %s", str(e.args))
        finally:
            cursor.close()
            db_conn.close()

    @classmethod
    def _executemany_in_decreasing_step(
            cls,
            cursor, sql: str, params: List[tuple],
            step: int, factor: float = 10
    ) -> list:
        if step <= 128:
            result = []
            for param in params:
                try:
                    cursor.execute(sql, param)
                    result += cursor.fetchall()
                except mysql.connector.IntegrityError:
                    pass

            return result

        result = []
        for i in range(0, len(params), step):
            try:
                cursor.executemany(sql, params[i:i+step])
                result += cursor.fetchall()
            except mysql.connector.IntegrityError:
                result += cls._executemany_in_decreasing_step(cursor, sql, params[i:i + step], int(step // factor))

        return result

    @classmethod
    def get_path_word_definition_folder(cls) -> str:
        config_path = os.path.join(pathlib.Path(__file__).parent.absolute(), "config", "word_definition")
        os.makedirs(config_path, exist_ok=True)

        return config_path

    @classmethod
    def parse_OPTED_json(cls, path_json_opted):
        try:
            _ = env_accessor.path_json_definition_all_word
        except decouple.UndefinedValueError:
            env_accessor.path_json_definition_all_word = os.path.join(
                cls.get_path_word_definition_folder(),
                'definition_all_word.json'
            )

        with open(path_json_opted, 'r') as opted_json:
            nouns = {}
            nulls = {}
            for entry in json.load(opted_json):
                word: str = entry['word'].lower()
                types: List[str] = entry['type'][1:-1].split(" ")
                description: str = entry['description'].split(".")[0] + "."

                if types is [] and nulls.get(word, None) is None:
                    nulls[word] = description
                elif 'n.' in types and nouns.get(word, None) is None:
                    nouns[word] = description

            formatted_json = {**nulls, **nouns}

            ref_start = "See "
            start_pos = len(ref_start)

            ref_delims = [",", ".", " and "]
            for word, description in formatted_json.items():
                if description.startswith(ref_start):
                    end_pos = min(filter(lambda x: x > 0, map(lambda x: description.find(x, start_pos), ref_delims)))

                    try:
                        ref_word = description[start_pos:end_pos]
                        description = formatted_json.get(ref_word, "")
                    except IndexError:
                        raise IndexError("OPTED json is not in the expected format.")

        with open(env_accessor.path_json_definition_all_word, 'w') as all_word_json:
            json.dump(formatted_json, all_word_json)

    @classmethod
    def create_definition_common_word_json(cls):
        try:
            _ = env_accessor.path_json_definition_common_word
        except decouple.UndefinedValueError:
            env_accessor.path_json_definition_common_word = os.path.join(
                cls.get_path_word_definition_folder(),
                'definition_common_word.json'
            )

        with open(env_accessor.path_yolov3_names, 'r') as yolov3_names:
            labels = {*yolov3_names.read().strip().split("\n")}

        # Connect to Dictionary
        db_conn = mysql.connector.connect(
            host=env_accessor.host_database,
            user=env_accessor.username_database,
            password=env_accessor.password_database,
            database=env_accessor.database_dictionary
        )
        cursor = db_conn.cursor()

        # Get description from database
        formatted_json = {}
        try:
            for label in labels:
                sql = """select word, description from Definition where word = %s"""
                param = (label,)
                cursor.execute(sql, param)

                description = cursor.fetchone() or ""
                formatted_json[label] = description
        except (mysql.connector.errors.OperationalError, mysql.connector.errors.ProgrammingError) as e:
            logging.warning("MySQLError during execute statement \n\tArgs: %s", str(e.args))

            # Get description from all word dictionary
            try:
                _ = env_accessor.path_json_definition_all_word
            except decouple.UndefinedValueError:
                cls.parse_OPTED_json(env_accessor.path_json_opted)

            with open(env_accessor.path_json_definition_all_word) as all_word_json:
                formatted_json = {
                    word: description for word, description in json.load(all_word_json) if word in labels
                }

        finally:
            cursor.close()
            db_conn.close()

        with open(env_accessor.path_json_definition_common_word, 'w') as common_word_json:
            json.dump(formatted_json, common_word_json)


if __name__ == '__main__':
    server_initializer = ServerInitializer()
    server_initializer.run()
