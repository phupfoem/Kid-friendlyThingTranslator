from env_access import env_accessor

import uvicorn

import logging

import argparse


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='Set up server.')
    parser.add_argument('-ip', '--ipv4', dest='ipv4', default="0.0.0.0")
    parser.add_argument('--ipv6', dest='ipv6', default="")
    parser.add_argument('-p', '--port', dest='port', type=int, default=8000)

    args = parser.parse_args()

    logging.basicConfig(level=env_accessor.log_mode)
    uvicorn.run("app.api:app", host=args.ipv6 or args.ipv4, port=args.port)
