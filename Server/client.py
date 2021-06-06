import requests

import decouple


if __name__ == "__main__":
    while True:
        email = input()
        password = input()

        base_url = "http://" + decouple.config('server_ip') + ":8000"
        url = base_url + '/user/login'
        json = {
            'email': email,
            'password': password
        }
        request = requests.post(url, json=json).json()

        print(request)

        try:
            while True:
                headers = {"Authorization": "Bearer " + request['access_token']}
                print(requests.post(base_url, headers=headers).json())

                if input() == 'q':
                    break
        except KeyError:
            while True:
                headers = {"Authorization": "Bearer " + "ABC"}
                print(requests.post(base_url, data="phu", headers=headers).json())

                if input() == 'q':
                    break
        if input() == 'q':
            break
