import cv2
import face_recognition
import pickle
import os
import time
import numpy as np

def recognize_face(admin_id):

    base_dir = f"encodings/admin_{admin_id}"

    if not os.path.exists(base_dir):
        print("❌ No encodings folder:", base_dir)
        return []

    known_encodings = []
    known_ids = []

    for file in os.listdir(base_dir):
        if file.endswith(".pkl"):
            person_id = int(file.replace("person_", "").replace(".pkl", ""))
            with open(os.path.join(base_dir, file), "rb") as f:
                encs = pickle.load(f)
                for e in encs:
                    known_encodings.append(e)
                    known_ids.append(person_id)

    if not known_encodings:
        print("❌ No face data inside folder")
        return []

    cap = cv2.VideoCapture(0)
    start = time.time()
    present_ids = set()

    print("📸 Attendance scan started...")

    while time.time() - start < 15:
        ret, frame = cap.read()
        if not ret:
            continue

        rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
        boxes = face_recognition.face_locations(rgb)

        encodings = face_recognition.face_encodings(rgb, boxes)

        for enc in encodings:
            matches = face_recognition.compare_faces(
                known_encodings, enc, tolerance=0.45
            )

            if True in matches:
                idx = matches.index(True)
                present_ids.add(known_ids[idx])

    cap.release()
    print("🚀 Sending response:", list(present_ids))
    return list(present_ids)
