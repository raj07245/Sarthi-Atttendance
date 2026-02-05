import face_recognition
import cv2
import os
import pickle
import time
from encoding_cache import update_cache


def register_face(admin_id, role="STUDENT"):

    base_dir = f"encodings/admin_{admin_id}/{role}"
    os.makedirs(base_dir, exist_ok=True)

    video = cv2.VideoCapture(0)

    if not video.isOpened():
        raise Exception("Camera not accessible")

    print("🚀 Smart registration started")

    person_id = int(time.time())  # unique id
    captured_encodings = []

    start = time.time()

    while len(captured_encodings) < 5:

        if time.time() - start > 40:
            break

        success, frame = video.read()
        if not success:
            continue

        # Resize = SPEED
        small = cv2.resize(frame, (0,0), fx=0.25, fy=0.25)
        rgb = cv2.cvtColor(small, cv2.COLOR_BGR2RGB)

        faces = face_recognition.face_locations(rgb)

        # Only ONE face allowed (anti proxy)
        if len(faces) != 1:
            continue

        encoding = face_recognition.face_encodings(rgb, faces)[0]
        captured_encodings.append(encoding)

        print(f"Captured frame {len(captured_encodings)}/5")

        time.sleep(0.4)

    video.release()
    cv2.destroyAllWindows()

    if len(captured_encodings) == 0:
        raise Exception("No face captured")

    # Average encoding = VERY ACCURATE
    final_encoding = sum(captured_encodings) / len(captured_encodings)

    file_path = f"{base_dir}/{person_id}.pkl"

    with open(file_path, "wb") as f:
        pickle.dump({
            "encoding": final_encoding,
            "person_id": person_id
        }, f)

    # 🔥 update RAM cache instantly
    update_cache(admin_id, role, final_encoding, person_id)

    print("✅ Face registered:", person_id)

    return [person_id]