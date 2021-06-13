import mysql.connector

import json
import decouple


if __name__ == "__main__":
    # Fetch all labels in yolov3 pretrained model
    labels = open(decouple.config("yolov3_names")).read().strip().split("\n")

    # Connect to DB Dictionary
    db_dictionary = mysql.connector.connect(
        host=decouple.config('host'),
        user=decouple.config('user'),
        password=decouple.config('password'),
        database=decouple.config('database_dictionary')
    )
    cursor = db_dictionary.cursor()

    # Fetch description for each label
    common_dictionary = {}
    for label in labels:
        sql = """select description from definition where word = %s"""
        param = (label,)

        cursor.execute(sql, param)
        description = cursor.fetchone()
        description = description[0] if description else ""

        common_dictionary[label] = description

    with open(decouple.config('common_word_definition'), 'w') as fp:
        json.dump(common_dictionary, fp)
