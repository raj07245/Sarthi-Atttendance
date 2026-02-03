import cv2
import face_recognition
import pickle
import os
import time

def register_face(admin_id, person_id):

    base_dir = f"encodings/admin_{admin_id}"
    os.makedirs(base_dir, exist_ok=True)

    cap = cv2.VideoCapture(0)
    if not cap.isOpened():
        print("❌ Camera not opened")
        return False

    encodings = []
    captures = 0
    start_time = time.time()

    while captures < 5:
        ret, frame = cap.read()
        if not ret:
            continue

        rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
        boxes = face_recognition.face_locations(rgb)

        if len(boxes) == 1:
            encoding = face_recognition.face_encodings(rgb, boxes)[0]
            encodings.append(encoding)
            captures += 1
            print(f"✅ Face captured {captures}/5")
            time.sleep(0.7)

        if time.time() - start_time > 20:
            break

    cap.release()

    if not encodings:
        return False

    file_path = f"{base_dir}/person_{person_id}.pkl"
    with open(file_path, "wb") as f:
        pickle.dump(encodings, f)

    print(f"✅ Face registered: {file_path}")
    return True
