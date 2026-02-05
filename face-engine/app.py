
from encoding_cache import load_all_encodings
from flask import Flask, jsonify
from register_face import register_face
from recognize_face import recognize_face

app = Flask(__name__)


# -------------------------
# Registration
# -------------------------
@app.post("/admin/<int:teacher_id>/register_face_students")
def register_students(teacher_id):
    try:
        ids = register_face(teacher_id, role="STUDENT")
        return jsonify({"registered_ids": ids, "total": len(ids)})
    except Exception as e:
        return jsonify({"registered_ids": [], "error": str(e)}), 500


@app.post("/admin/<int:manager_id>/register_face_employees")
def register_employees(manager_id):
    try:
        ids = register_face(manager_id, role="EMPLOYEE")
        return jsonify({"registered_ids": ids, "total": len(ids)})
    except Exception as e:
        return jsonify({"registered_ids": [], "error": str(e)}), 500


# -------------------------
# Attendance
# -------------------------
@app.post("/admin/<int:teacher_id>/attendance_students")
def attendance_students(teacher_id):
    try:
        matched_ids = recognize_face(teacher_id, role="STUDENT")
        return jsonify({"matched": matched_ids, "count": len(matched_ids)})
    except Exception as e:
        return jsonify({"matched": [], "error": str(e)}), 500


@app.post("/admin/<int:manager_id>/attendance_employees")
def attendance_employees(manager_id):
    try:
        matched_ids = recognize_face(manager_id, role="EMPLOYEE")
        return jsonify({"matched": matched_ids, "count": len(matched_ids)})
    except Exception as e:
        return jsonify({"matched": [], "error": str(e)}), 500


if __name__ == "__main__":
    load_all_encodings()   # 🔥 LOAD INTO RAM
    app.run(port=5001, debug=True)