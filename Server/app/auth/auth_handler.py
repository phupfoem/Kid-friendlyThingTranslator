import jwt
# noinspection PyPackageRequirements
import decouple

import time
from typing import Dict, Optional


__jwt_secret = decouple.config("secret")
__jwt_algo = decouple.config("algorithm")


def token_response(token: bytes):
    print("Token JSON-ified")
    return {
        "access_token": token
    }


def sign_jwt(sth: str) -> Dict[str, bytes]:
    print("Generate a token")
    payload = {
        "data": sth,
        "exp": time.time() + 3600
    }
    token = jwt.encode(payload, __jwt_secret, algorithm=__jwt_algo)

    return token_response(token)


def decode_jwt(token: str) -> Optional[dict]:
    try:
        print("Try decoding jwt")
        return jwt.decode(token, __jwt_secret, algorithms=[__jwt_algo], require=["exp"], verify_exp=True)
    except:
        print("Decoding failed")
        return None
