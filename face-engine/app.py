from flask import Flask, jsonify
from register_face import register_face
from recognize_face import recognize_face

app = Flask(__name__)


@app.post("/admin/<int:admin_id>/register_face")
def auto_register_api(admin_id):

    try:
        ids = auto_register(admin_id)

        return jsonify({
            "registered_ids": ids,
            "total": len(ids)
        })

    except Exception as e:
        return jsonify({"error": str(e)}), 500

@app.post("/admin/<int:admin_id>/attendance")
def attendance(admin_id):

    try:
        matched_ids = recognize_faces(admin_id)

        return jsonify({
            "matched": matched_ids,     # ⭐ IMPORTANT (Spring expects this)
            "count": len(matched_ids)
        })

    except Exception as e:
        return jsonify({
            "matched": [],
            "error": str(e)
        }), 500


if __name__ == "__main__":
    app.run(port=5001, debug=True)
