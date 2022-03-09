'''
  To run this server:
  export FLASK_APP=hello
  export FLASK_ENV=development
  flask run
'''

from flask import Flask
import sqlite3

app = Flask(__name__)


def getSqliteConnection():
    connection = sqlite3.connect('database.db')
    connection.row_factory = sqlite3.Row
    return connection


@app.route('/register', methods=('POST'))
def register():
    if request.method == 'POST':
        username = request.form.get('username')
        password = request.form.get('password')
        conn = getSqliteConnection()
        conn.execute('INSERT INTO user(username, password) VALUES (?,?)',
                     (username, password))
        conn.commit()
        conn.close()
        return {}.to_json()
    else:
        return {}.to_json()


@app.route('/login', methods=('POST'))
def login():
    if request.method == 'GET':
        username = request.form.get('username')
        password = request.form.get('password')
        conn = getSqliteConnection()
        data = conn.execute(
            'SELECT * FROM user WHERE username=(?) and password=(?)',
            (username, password)).fetchall()[0]
        conn.commit()
        conn.close()

        return {username: data['username'], password: data['password']}.to_json()
