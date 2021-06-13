from dictionary import Dictionary

import json


class MemoryDictionary(Dictionary):
    def __init__(self, json_path: str):
        self._map = {
            entry["word"]: entry["description"] for entry in json.load(open(json_path))
        }

    def define(self, word: str) -> str:
        try:
            return self._map[word.lower()]
        except KeyError:
            return ""
