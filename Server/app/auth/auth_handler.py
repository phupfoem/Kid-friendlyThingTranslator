"""
MIT License

Copyright (c) 2020 Michael Herman, 2021 Phú Nguyễn Hữu Thiên

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
© 2021 GitHub, Inc.
"""
import logging

from env_access import env_accessor

import jwt

import time

from typing import Dict, Optional, Union


__jwt_secret = env_accessor.key_to_encrypt
__jwt_algo = env_accessor.algorithm_to_encrypt


def token_response(token: Union[bytes, str]):
    logging.debug("Token JSON-ified")
    return {
        'access_token': token
    }


def sign_jwt(sth: str) -> Dict[str, bytes]:
    logging.debug("Generate a token")
    payload = {
        'data': sth,
        'exp': time.time() + 60 * 60 * 24
    }
    token = jwt.encode(payload, __jwt_secret, algorithm=__jwt_algo)

    return token_response(token)


def decode_jwt(token: str) -> Optional[dict]:
    try:
        logging.debug("Try decoding jwt")

        return jwt.decode(token, __jwt_secret, algorithms=[__jwt_algo], require=["exp"], verify_exp=True)
    except jwt.PyJWTError:
        logging.warning("Decoding failed")
        return None
