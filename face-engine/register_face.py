import face_recognition
import cv2
import pickle
import os
import pyttsx3
import time

engine = pyttsx3.init()

def speak(text):
    engine.say(text)
    engine.runAndWait()


def register_face(admin_id):

    enc_path = f"encodings/admin_{admin_id}.pkl"

    # Load existing encodings
    if os.path.exists(enc_path):

        with open(enc_path, "rb") as f:
            data = pickle.load(f)

        known_encodings = data["encodings"]
        known_ids = data["ids"]

    else:
        known_encodings = []
        known_ids = []

    next_id = len(known_ids) + 1

    video = cv2.VideoCapture(0, cv2.CAP_AVFOUNDATION)

    if not video.isOpened():
        raise Exception("Camera not accessible")

    print("🚀 FULLY AUTOMATIC ENROLLMENT STARTED")
    speak("Face registration started")

    last_capture_time = 0
    COOLDOWN = 3
    MAX_NEW_FACES = 5
    registered_now = 0   # ✅ FIXED

    # ✅ TIMEOUT SET
    start_time = time.time()
    TIMEOUT = 45   # Recommended

    while registered_now < MAX_NEW_FACES:

        # ✅ TIMEOUT CHECK (MOST IMPORTANT)
        if time.time() - start_time > TIMEOUT:
            print("⏰ Registration timeout")
            speak("Registration timeout")
            break

        success, frame = video.read()

        if not success or frame is None:
            continue

        try:
            rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
            faces = face_recognition.face_locations(rgb)

        except Exception as e:
            print("OpenCV crash prevented:", e)
            continue

        current_time = time.time()

        if len(faces) == 1 and (current_time - last_capture_time) > COOLDOWN:

            encodings = face_recognition.face_encodings(rgb, faces)

            if len(encodings) > 0:

                new_encoding = encodings[0]

                if len(known_encodings) > 0:

                    matches = face_recognition.compare_faces(
                        known_encodings,
                        new_encoding,
                        tolerance=0.5
                    )

                    if True in matches:
                        print("⚠️ Face already registered")
                        last_capture_time = current_time
                        continue

                known_encodings.append(new_encoding)
                known_ids.append(next_id)

                print(f"✅ User Registered -> ID: {next_id}")
                speak("Face captured")

                registered_now += 1
                next_id += 1
                last_capture_time = current_time

    # ✅ VERY IMPORTANT (camera crash prevention)
    video.release()
    cv2.destroyAllWindows()

    data = {
        "encodings": known_encodings,
        "ids": known_ids
    }

    os.makedirs("encodings", exist_ok=True)

    with open(enc_path, "wb") as f:
        pickle.dump(data, f)

    speak("All faces saved")
    print("💾 Encodings stored successfully")

    return known_ids