from flask import Flask, jsonify
from register_face import register_face
from recognize_face import recognize_face

app = Flask(__name__)

@app.post("/admin/<int:admin_id>/register/<int:person_id>")
def register(admin_id, person_id):
    success = register_face(admin_id, person_id)
    return jsonify({"success": success})

@app.post("/admin/<int:admin_id>/attendance")
def attendance(admin_id):
    matched = recognize_face(admin_id)
    return jsonify({"present_ids": matched})

if __name__ == "__main__":
    app.run(port=5001, debug=True)
