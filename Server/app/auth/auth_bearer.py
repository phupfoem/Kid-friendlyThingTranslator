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

from .auth_handler import decode_jwt

from fastapi import HTTPException, Request
from fastapi.security import HTTPBearer, HTTPAuthorizationCredentials

import logging


class JWTBearer(HTTPBearer):
    SCHEME: str = 'Bearer'

    def __init__(self, auto_error: bool = True):
        super().__init__(auto_error=auto_error)

    async def __call__(self, request: Request):
        logging.debug("HTTP Base Check Started")
        credentials: HTTPAuthorizationCredentials = await super().__call__(request)
        logging.debug("HTTP Base Check Ended")

        logging.debug("Credentials: " + str(credentials))
        if credentials:
            if not credentials.scheme == self.SCHEME:
                logging.debug(' '.join(["Scheme not supported, please use scheme", self.SCHEME]))
                raise HTTPException(status_code=403, detail="Invalid authentication scheme.")
            if not self.verify_jwt(credentials.credentials):
                logging.debug("Invalid token, need to log in again")
                raise HTTPException(status_code=403, detail="Invalid token or expired token.")
            return credentials.credentials
        else:
            logging.debug("Invalid authorization on HTTP Base Check")
            raise HTTPException(status_code=403, detail="Invalid authorization code.")

    @classmethod
    def verify_jwt(cls, jwt_token: str) -> bool:
        try:
            payload = decode_jwt(jwt_token)
        except:
            payload = None

        return payload is not None
