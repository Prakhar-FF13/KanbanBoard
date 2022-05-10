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


@app.route('/workspaces', methods=['POST'])
def workspaces():
    if request.method == 'POST':
        x = json.loads(request.data)
        print(x)
        username = x['username']
        conn = getSqliteConnection()
        data = conn.execute(
            'SELECT * FROM workspaceusers WHERE username=(?)',
            (username,)).fetchall()
        data1 = conn.execute(
            'SELECT * FROM workspacecollaborators WHERE username_collaborators=(?)',
            (username,)).fetchall()
        # if (len(data) == 0):
        #     return json.dumps({"code": 200, "message": "No workspaces found"})
        

        workspaces = []
        # for d in data:
        #     x = conn.execute(
        #         'SELECT * FROM workspaces WHERE wid=(?)',
        #         (d["wid"], )).fetchall()
        #     workspaces.append({
        #         "wid": x[0]["wid"],
        #         "name": x[0]["name"],
        #         "createdBy": x[0]["createdBy"],
        #         "members": x[0]["members"]
        #     })
        colabdata = []
        if len(data1)!=0:
           
            for d in data1:
                colabdata.append(d["wid"])
        if len(data)!=0:
            for d in data:
                colabdata.append(d["wid"])
        colabdata = set(colabdata)
        if len(colabdata)!=0:    
            for mwid in colabdata:
                x = conn.execute(
                    'SELECT * FROM workspaces WHERE wid=(?)',
                    (mwid, )).fetchall()
                workspaces.append({
                    "wid": x[0]["wid"],
                    "name": x[0]["name"],
                    "createdBy": x[0]["createdBy"],
                    "members": x[0]["members"]
                })                    

        print(workspaces)

        conn.commit()
        conn.close()

        return json.dumps({"code": 200, "message": "Workspaces found", "workspaces": workspaces})
    else:
        return json.dumps({"code": 400, "message": "Failed to get workspaces"})


@app.route('/createworkspace', methods=['POST'])
def createworkspace():
    if request.method == 'POST':
        x = json.loads(request.data)
        print(x)
        createdBy = x['createdBy']
        name = x['name']
        conn = getSqliteConnection()
        conn.execute(
            "INSERT INTO WORKSPACES(name,createdBy) VALUES(?,?)", (name, createdBy))
        
        data = conn.execute(
            "SELECT * FROM WORKSPACES WHERE name=(?) AND createdBy=(?)", (name, createdBy)).fetchall()
        conn.execute(
            "INSERT INTO workspacecollaborators(wid,leader,username_collaborators) VALUES(?,?,?)", (data[0]["wid"], data[0]["createdBy"],data[0]["createdBy"]))
        conn.execute(
            "INSERT INTO WORKSPACEUSERS VALUES(?,?)", (
                data[0]["wid"], data[0]["createdBy"])
        )
        conn.commit()
        conn.close()

        return json.dumps({
            "code": 200,
            "message": "Workspaces created",
            "wid": data[0]["wid"],
            "name": data[0]["name"],
            "createdBy": data[0]["createdBy"],
            "members": data[0]["members"],
        })
    else:
        return json.dumps({"code": 400, "message": "Failed to create workspace"})


@app.route('/workspacetasks', methods=['POST'])
def workspacetasks():
    if request.method == 'POST':
        x = json.loads(request.data)
        print(x)
        wid = x['wid']
        status = x['status']
        conn = getSqliteConnection()
        data = conn.execute(
            'SELECT * FROM workspaceTasks WHERE wid=(?) AND status=(?)',
            (wid, status)).fetchall()

        

        if (len(data) == 0):
            return json.dumps({"code": 200, "message": "No workspace task found"})

        workspaceTasks = []
        for d in data:
            workspaceTasks.append({
                "id": d["id"],
                "title": d["title"],
                "description": d["description"],
                "priority": d["priority"],
                "assignee": d["assignee"],
                "status": d["status"],
            })

        print(workspaceTasks)

        conn.commit()
        conn.close()

        return json.dumps({"code": 200, "message": "Workspace Tasks found", "workspaceTasks": workspaceTasks})
    else:
        return json.dumps({"code": 400, "message": "Failed to get workspace tasks"})


@app.route('/createworkspacetask', methods=['POST'])
def createworkspacetask():
    if request.method == 'POST':
        x = json.loads(request.data)
        print(x)
        wid = x["wid"]
        title = x["title"]
        description = x["description"]
        priority = x["priority"]
        assignee = x["assignee"]
        status = x["status"]
        conn = getSqliteConnection()
        cursor = conn.cursor()
        cursor.execute(
            'INSERT INTO WORKSPACETASKS(wid, title, description, assignee, status, priority) VALUES(?,?,?,?,?,?)',
            (wid, title, description, assignee, status, priority))
        id = cursor.lastrowid
        conn.commit()
        conn.close()

        return json.dumps({"code": 200, "message": "Workspace Task Created", "id": id})
    else:
        return json.dumps({"code": 400, "message": "Failed to create workspace task"})


@app.route('/updateworkspacetask', methods=['POST'])
def updateworkspacetask():
    if request.method == 'POST':
        x = json.loads(request.data)
        print(x)
        id = x["id"]
        title = x["title"]
        description = x["description"]
        priority = x["priority"]
        assignee = x["assignee"]
        status = x["status"]
        conn = getSqliteConnection()
        conn.execute(
            'UPDATE WORKSPACETASKS SET title=(?), description=(?), priority=(?), assignee=(?), status=(?) WHERE id=(?)',
            (title, description, priority, assignee, status, id))
        conn.commit()
        conn.close()

        return json.dumps({"code": 200, "message": "Workspace Task Updated"})
    else:
        return json.dumps({"code": 400, "message": "Failed to update workspace task"})


@app.route('/addcollaborators', methods=['POST'])
def addcollaborators():
    if request.method == 'POST':
        x = json.loads(request.data)
        print(x)
        wid = x['wid']
        leader = x['leader']
        username_collaborators = x['username_collaborators']
        conn = getSqliteConnection()
        conn.execute(
            "INSERT INTO workspacecollaborators(wid,leader,username_collaborators) VALUES(?,?,?)", (wid, leader, username_collaborators))
        data =  conn.execute(
            "SELECT * FROM WORKSPACES WHERE wid=(?)", (wid, )).fetchall()
        prev = data[0]['members']
        conn.execute("UPDATE WORKSPACES set members=(?) where wid =(?)",(prev+1, wid))
        conn.commit()
        conn.close()

        return json.dumps({
            "code": 200,
            "message": "Collaborators added",
            "wid": x["wid"],
            "leader": x["leader"],
            "username_collaborators": x["username_collaborators"],
            
        })
    else:
        return json.dumps({"code": 400, "message": "Failed to create workspace"})


@app.route('/showcollaborators', methods=['POST'])
def showcollaborators():
    if request.method == 'POST':
        x = json.loads(request.data)
        print(x)
        wid = x['wid']
        conn = getSqliteConnection()
        data = conn.execute(
            "SELECT * FROM workspacecollaborators WHERE wid=(?)",
            (wid,)).fetchall()
        
        if (len(data) == 0):
            return json.dumps({"code": 200, "message": "No workspaces found"})
        

        collabs = []
        for d in data:
            collabs.append({
                "wid": d["wid"],
                "leader": d["leader"],
                "collab_name": d["username_collaborators"]
            })
                       

        print(collabs)

        conn.commit()
        conn.close()

        return json.dumps({"code": 200, "message": "collaborators found", "collabs": collabs})
    else:
        return json.dumps({"code": 400, "message": "Failed to get Collaborators"})

@app.route('/removecollab', methods=['POST'])
def removecollab():
    if request.method=='POST':
        x = json.loads(request.data)
        wid = x['wid']
        username = x['username']
        conn = getSqliteConnection()
        conn.execute('DELETE FROM workspacecollaborators WHERE wid=(?) AND username_collaborators=(?)',(wid,username))
        data =  conn.execute(
            "SELECT * FROM WORKSPACES WHERE wid=(?)", (wid, )).fetchall()
        prev = data[0]['members']
        conn.execute("UPDATE WORKSPACES set members=(?) where wid =(?)",(prev-1, wid))
        conn.commit()
        conn.close()
        return json.dumps({"code": 200, "message": "Collabarators deleted success "})
    else:
        return json.dumps({"code": 400, "message": "Failed to delete collab  deletion"})