from word_definition.dictionary import Dictionary

from mysql.connector.errors import OperationalError, ProgrammingError

import logging


class DatabaseDictionary(Dictionary):
    def __init__(self, conn):
        self._conn = conn

    def define(self, word: str) -> str:
        try:
            cursor = self._conn.cursor()

            sql = "SELECT description FROM Definition WHERE word = %s"
            param = (word,)

            cursor.execute(sql, param)

            result = cursor.fetchone()
            cursor.close()

            if result is None:
                return ""

            return result[0]
        except (OperationalError, ProgrammingError) as e:
            logging.warning("MySQLError during execute statement \n\tArgs: %s", str(e.args))
            return ""
