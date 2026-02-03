import face_recognition
import cv2
import os
import pickle


def recognize_face(admin_id):

    enc_path = f"encodings/admin_{admin_id}.pkl"

    if not os.path.exists(enc_path):
        raise Exception("No encodings found. Register faces first.")

    # ✅ load encodings
    with open(enc_path, "rb") as f:
        data = pickle.load(f)

    known_encodings = data["encodings"]
    known_ids = data["ids"]

    video = cv2.VideoCapture(0)

    print("📸 Multi-face attendance started...")

    detected_ids = set()

    frame_count = 0
    MAX_FRAMES = 20   # ⭐ scan few frames for accuracy

    while frame_count < MAX_FRAMES:

        success, frame = video.read()

        if not success:
            continue

        rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)

        faces = face_recognition.face_locations(rgb)
        encodings = face_recognition.face_encodings(rgb, faces)

        for face_encoding in encodings:

            matches = face_recognition.compare_faces(
                known_encodings,
                face_encoding,
                tolerance=0.5   # ⭐ tweak later
            )

            if True in matches:

                idx = matches.index(True)
                person_id = known_ids[idx]

                detected_ids.add(person_id)

        frame_count += 1

    video.release()
    cv2.destroyAllWindows()

    print("✅ Detected:", detected_ids)

    return list(detected_ids)