import mysql.connector

import json
import decouple


if __name__ == "__main__":
    # Create db User and Dictionary
    cursor = mysql.connector.connect(
        host=decouple.config('host'),
        user=decouple.config('user'),
        password=decouple.config('password')
    ).cursor()

    sql = """create database User"""
    cursor.execute(sql)

    sql = """create database Dictionary"""
    cursor.execute(sql)

    # Create in User, table Accounts
    db_user = mysql.connector.connect(
        host=decouple.config('host'),
        user=decouple.config('user'),
        password=decouple.config('password'),
        database=decouple.config('database_user')
    )
    cursor = db_user.cursor()

    sql = """create table Accounts(
                username varchar(128) not null,
                password varchar(128) not null,
                name varchar(128) not null)"""
    cursor.execute(sql)

    sql = """create unique index Accounts_username_uindex
                on Accounts (username)"""
    cursor.execute(sql)

    sql = """alter table Accounts
                add constraint Accounts_pk
                    primary key (username)"""
    cursor.execute(sql)

    # Populate Accounts
    sql = """insert into Accounts (username, password, name) values
                ('phu@phu.com', 'pass1234', 'Pfoem')"""
    cursor.execute(sql)
    db_user.commit()

    # Create in Dictionary, table Definition
    db_dictionary = mysql.connector.connect(
        host=decouple.config('host'),
        user=decouple.config('user'),
        password=decouple.config('password'),
        database=decouple.config('database_dictionary')
    )
    cursor = db_dictionary.cursor()

    sql = """create table Definition(
                word varchar(100) not null,
                description varchar(1000) not null,
                constraint Definition_pk
                    primary key (word))"""
    cursor.execute(sql)

    # Populate Definition
    sql = """insert into Definition (word, description) values
                (%s, %s)"""
    params = [*{entry['word'].lower(): entry['description']
              for entry in json.load(open(decouple.config('word_definition')))
              if "n." in entry['type']}.items()]

    step = 1000
    for i in range(0, len(params), step):
        cursor.executemany(sql, params[i:i+step])

    db_dictionary.commit()
