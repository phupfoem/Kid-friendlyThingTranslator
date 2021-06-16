from word_definition.dictionary import Dictionary

import json


class MemoryDictionary(Dictionary):
    def __init__(self, json_path: str):
        with open(json_path, 'r') as json_file:
            self._map = json.load(json_file)

    def define(self, word: str) -> str:
        try:
            return self._map[word.lower()]
        except KeyError:
            return ""
