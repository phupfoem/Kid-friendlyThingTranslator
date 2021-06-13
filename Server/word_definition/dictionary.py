from abc import ABC, abstractmethod


class Dictionary(ABC):
    @abstractmethod
    def define(self, word: str) -> str:
        pass
