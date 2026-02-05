import os
import pickle

ENCODING_CACHE = {}
BASE_DIR = "encodings"


def load_all_encodings():

    if not os.path.exists(BASE_DIR):
        return

    for admin_folder in os.listdir(BASE_DIR):

        admin_path = os.path.join(BASE_DIR, admin_folder)

        if not os.path.isdir(admin_path):
            continue

        admin_id = admin_folder.split("_")[1]

        ENCODING_CACHE[admin_id] = {}

        for role in os.listdir(admin_path):

            role_path = os.path.join(admin_path, role)

            encodings = []
            ids = []

            for file in os.listdir(role_path):

                file_path = os.path.join(role_path, file)

                with open(file_path, "rb") as f:
                    data = pickle.load(f)

                    encodings.append(data["encoding"])
                    ids.append(data["person_id"])

            ENCODING_CACHE[admin_id][role] = {
                "encodings": encodings,
                "ids": ids
            }

    print("🔥 ALL ENCODINGS LOADED INTO RAM")


def get_cached(admin_id, role):

    admin_id = str(admin_id)

    if admin_id not in ENCODING_CACHE:
        return None, None

    if role not in ENCODING_CACHE[admin_id]:
        return None, None

    data = ENCODING_CACHE[admin_id][role]

    return data["encodings"], data["ids"]


def update_cache(admin_id, role, encoding, person_id):

    admin_id = str(admin_id)

    if admin_id not in ENCODING_CACHE:
        ENCODING_CACHE[admin_id] = {}

    if role not in ENCODING_CACHE[admin_id]:
        ENCODING_CACHE[admin_id][role] = {
            "encodings": [],
            "ids": []
        }

    ENCODING_CACHE[admin_id][role]["encodings"].append(encoding)
    ENCODING_CACHE[admin_id][role]["ids"].append(person_id)