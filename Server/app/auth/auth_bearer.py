from fastapi import HTTPException, Request
from fastapi.security import HTTPBearer, HTTPAuthorizationCredentials

from .auth_handler import decode_jwt


class JWTBearer(HTTPBearer):
    def __init__(self, auto_error: bool = True):
        super().__init__(auto_error=auto_error)

    async def __call__(self, request: Request):
        print("HTTP Base Check Started")
        credentials: HTTPAuthorizationCredentials = await super().__call__(request)
        print("HTTP Base Check Ended")

        print("Credentials: " + str(credentials))
        if credentials:
            if not credentials.scheme == "Bearer":
                print("Scheme not supported, please use scheme" + "Bearer")
                raise HTTPException(status_code=403, detail="Invalid authentication scheme.")
            if not self.verify_jwt(credentials.credentials):
                print("Invalid token, need to log in again")
                raise HTTPException(status_code=403, detail="Invalid token or expired token.")
            return credentials.credentials
        else:
            print("Invalid authorization on HTTP Base Check")
            raise HTTPException(status_code=403, detail="Invalid authorization code.")

    @classmethod
    def verify_jwt(cls, jwt_token: str) -> bool:
        try:
            payload = decode_jwt(jwt_token)
        except:
            payload = None

        return payload is not None
