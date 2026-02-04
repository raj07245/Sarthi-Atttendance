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

    video = cv2.VideoCapture(0, cv2.CAP_AVFOUNDATION)

    if not video.isOpened():
        raise Exception("Camera not accessible")

    print("📸 Multi-face attendance started...")

    detected_ids = set()
    frame_count = 0
    MAX_FRAMES = 30   # scan few frames for accuracy

    while frame_count < MAX_FRAMES:

        success, frame = video.read()

        if not success or frame is None:
            print("Frame not captured properly...")
            continue

        # Convert to RGB
        rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)

        # Detect faces
        faces = face_recognition.face_locations(rgb)
        encodings = face_recognition.face_encodings(rgb, faces)

        for face_encoding in encodings:

            face_distances = face_recognition.face_distance(
                known_encodings,
                face_encoding
            )

            best_match_index = face_distances.argmin()

            if face_distances[best_match_index] < 0.5:
                person_id = known_ids[best_match_index]
                detected_ids.add(person_id)

        frame_count += 1

        # Press ESC to stop
        if cv2.waitKey(1) == 27:
            break

    video.release()
    cv2.destroyAllWindows()

    print("✅ Detected:", detected_ids)

    return list(detected_ids)