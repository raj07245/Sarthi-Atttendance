import face_recognition
import cv2
from encoding_cache import get_cached


def recognize_face(admin_id, role="STUDENT"):

    known_encodings, known_ids = get_cached(admin_id, role)

    if not known_encodings:
        raise Exception("No encodings found. Register faces first.")

    video = cv2.VideoCapture(0)

    if not video.isOpened():
        raise Exception("Camera not accessible")

    print("📸 ULTRA FAST attendance started...")

    detected_ids = set()

    process_this_frame = True
    frame_count = 0

    while frame_count < 30:

        success, frame = video.read()

        if not success:
            continue

        if process_this_frame:

            small = cv2.resize(frame, (0,0), fx=0.25, fy=0.25)
            rgb = cv2.cvtColor(small, cv2.COLOR_BGR2RGB)

            faces = face_recognition.face_locations(rgb)
            encodings = face_recognition.face_encodings(rgb, faces)

            for face_encoding in encodings:

                distances = face_recognition.face_distance(
                    known_encodings,
                    face_encoding
                )

                best_match_index = distances.argmin()

                if distances[best_match_index] < 0.45:
                    detected_ids.add(known_ids[best_match_index])

        process_this_frame = not process_this_frame
        frame_count += 1

    video.release()
    cv2.destroyAllWindows()

    print("✅ Detected:", detected_ids)

    return list(detected_ids)