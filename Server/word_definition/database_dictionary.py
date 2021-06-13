from word_definition.dictionary import Dictionary

import json


class DatabaseDictionary(Dictionary):
    def __init__(self, conn):
        self._conn = conn

    def define(self, word: str) -> str:
        try:
            cursor = self._conn.cursor()

            sql = "SELECT description FROM Definition WHERE word = %s"
            param = (word,)

            cursor.execute(sql, param)

            return cursor.fetchone()[0]
        except TypeError:
            return ""
