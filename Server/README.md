# Securing FastAPI with JWT Token-based Authentication

### Want to learn how to build this?

Check out the [post](https://testdriven.io/blog/fastapi-jwt-auth/).

## Want to use this project?

1. Fork/Clone

1. Create and activate a virtual environment:

    ```sh
    $ python3 -m venv venv && source venv/bin/activate
    ```

1. Install the requirements:

    ```sh
    (venv)$ pip install -r requirements.txt
    ```
1. Change settings of database in *config.yml*

1. Run the app:

    ```sh
    (venv)$ python main.py
    ```
   
   or
   ```sh
   uvicorn app.api:app --host 0.0.0.0 --port 8000 --reload
   ```


1. Test at [http://localhost:8000](http://localhost:8000)
