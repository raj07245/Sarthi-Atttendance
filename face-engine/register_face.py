import face_recognition
import cv2
import pickle
import os
import pyttsx3
import time

def speak(text):
    engine = pyttsx3.init()
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

    video = cv2.VideoCapture(0)

    print("🚀 FULLY AUTOMATIC ENROLLMENT STARTED")

    speak("Face registration started")

    last_capture_time = 0
    COOLDOWN = 3   # seconds between captures (VERY IMPORTANT)

    while True:

        success, frame = video.read()
        if not success:
            continue

        rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)

        faces = face_recognition.face_locations(rgb)

        # Draw rectangle
        for (top, right, bottom, left) in faces:
            cv2.rectangle(frame, (left, top), (right, bottom), (0,255,0), 2)

        cv2.imshow("Auto Enrollment", frame)

        current_time = time.time()

        # AUTO CAPTURE LOGIC
        if len(faces) == 1 and (current_time - last_capture_time) > COOLDOWN:

            encodings = face_recognition.face_encodings(rgb, faces)

            if len(encodings) > 0:

                new_encoding = encodings[0]

                # CHECK duplicate face
                if len(known_encodings) > 0:

                    matches = face_recognition.compare_faces(
                        known_encodings,
                        new_encoding,
                        tolerance=0.5
                    )

                    if True in matches:
                        print("⚠️ Face already registered")
                        speak("Face already registered")
                        last_capture_time = current_time
                        continue

                # SAVE NEW FACE
                known_encodings.append(new_encoding)
                known_ids.append(next_id)

                print(f"✅ User Registered -> ID: {next_id}")
                speak("Registered. Next person please")

                next_id += 1
                last_capture_time = current_time

        # Exit with ESC
        if cv2.waitKey(1) == 27:
            break

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