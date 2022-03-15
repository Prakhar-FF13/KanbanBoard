'''
  To run this server:
  export FLASK_APP=server
  export FLASK_ENV=development
  flask run
'''

from flask import Flask, request
import sqlite3
import json

app = Flask(__name__)


def initDB():
    connection = sqlite3.connect('database.db')

    with open('schema.sql') as f:
        connection.executescript(f.read())

    connection.commit()
    connection.close()


initDB()


def getSqliteConnection():
    connection = sqlite3.connect('database.db')
    connection.row_factory = sqlite3.Row
    return connection


@app.route('/register', methods=['POST'])
def register():
    if request.method == 'POST':
        x = json.loads(request.data)
        print(x)
        username = x['username']
        password = x['password']
        conn = getSqliteConnection()
        conn.execute('INSERT INTO users(username, password) VALUES (?,?)',
                     (username, password))
        conn.commit()
        conn.close()
        return json.dumps({"code": 200, "message": "registration successfull"})
    else:
        return json.dumps({"code": 400, "message": "registration failed"})


@app.route('/login', methods=['POST'])
def login():
    if request.method == 'POST':
        x = json.loads(request.data)
        print(x)
        username = x['username']
        password = x['password']
        conn = getSqliteConnection()
        data = conn.execute(
            'SELECT * FROM users WHERE username=(?) and password=(?)',
            (username, password)).fetchall()
        conn.commit()
        conn.close()

        if (len(data) == 0):
            return json.dumps({"code": 400, "message": "User not found"})

        data = data[0]

        return json.dumps({"code": 200, "message": "Logged In", "username": data['username'], "password": data['password']})
    else:
        return json.dumps({"code": 400, "message": "Login Failed"})


@app.route('/forgot', methods=['POST'])
def forgot():
    if request.method == 'POST':
        x = json.loads(request.data)
        print(x)
        username = x['username']
        password = x['password']
        conn = getSqliteConnection()
        data = conn.execute(
            'SELECT * FROM users WHERE username=(?)',
            (username,)).fetchall()
        conn.commit()
        conn.close()

        if (len(data) == 0):
            return json.dumps({"code": 400, "message": "User not found"})

        data = data[0]
        conn = getSqliteConnection()
        cur = conn.cursor()
        cur.execute('UPDATE users SET password=(?) WHERE username=(?)',
                    (password, username))
        conn.commit()
        conn.close()

        return json.dumps({"code": 200, "message": "Password Updated"})
