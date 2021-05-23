import mysql.connector

import decouple


if __name__ == "__main__":
    my_db = mysql.connector.connect(
        host=decouple.config('host'),
        user=decouple.config('user'),
        password=decouple.config('password')
    )

    my_cursor = my_db.cursor()

    sql = """create database USER"""
    my_cursor.execute(sql)

    my_db = mysql.connector.connect(
        host=decouple.config('host'),
        user=decouple.config('user'),
        password=decouple.config('password'),
        database=decouple.config('database_user')
    )

    my_cursor = my_db.cursor()

    sql = """create table Accounts(
                username varchar(128) not null,
                password varchar(128) not null)"""
    my_cursor.execute(sql)

    sql = """create unique index Accounts_username_uindex
                on Accounts (username)"""
    my_cursor.execute(sql)

    sql = """alter table Accounts
                add constraint Accounts_pk
                    primary key (username)"""
    my_cursor.execute(sql)

    sql = """insert into Accounts (username, password) values
                ('admin', 'admin'),
                ('user', 'pass')"""
    my_cursor.execute(sql)
    my_db.commit()
