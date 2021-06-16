import uvicorn

import requests

import decouple

if __name__ == "__main__":
    while True:
        email = input()
        password = input()

        base_url = "http://localhost:8000"
        login_url = base_url + '/user/login'
        image_url = base_url + '/label-image'
        json = {
            'email': email,
            'password': password
        }
        request = requests.post(login_url, json=json).json()

        print(request)
        print(type(request['access_token']))

        try:
            while True:
                headers = {"Authorization": "Bearer " + request['access_token']}
                print(requests.post(image_url, headers=headers).json())

                if input() == 'q':
                    break
        except KeyError:
            while True:
                headers = {"Authorization": "Bearer " + "ABC"}
                print(requests.post(image_url, data="phu", headers=headers).json())

                if input() == 'q':
                    break
        if input() == 'q':
            break
