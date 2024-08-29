import face_recognition
import cv2
import numpy as np


def load_and_check_image(image_path):
    # Load image using OpenCV
    image = cv2.imread(image_path)

    if image is None:
        raise ValueError(f"Could not load image: {image_path}")

    # Check if the image is in RGB format, if not, convert it
    if len(image.shape) == 2:  # Grayscale image
        print("Converting grayscale to RGB.")
        image = cv2.cvtColor(image, cv2.COLOR_GRAY2RGB)
    elif image.shape[2] == 4:  # Has an alpha channel (e.g., PNG with transparency)
        print("Removing alpha channel.")
        image = cv2.cvtColor(image, cv2.COLOR_BGRA2RGB)
    elif image.shape[2] == 3:  # Already in BGR format, just convert to RGB
        image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)

    return image


def compare_faces(image_path1, image_path2):
    # Load and check images
    image1 = load_and_check_image(image_path1)
    image2 = load_and_check_image(image_path2)

    # Find face encodings in both images
    face_encodings1 = face_recognition.face_encodings(image1)
    face_encodings2 = face_recognition.face_encodings(image2)

    # Ensure both images have at least one face detected
    if len(face_encodings1) == 0:
        raise ValueError(f"No faces found in image1: {image_path1}")
    if len(face_encodings2) == 0:
        raise ValueError(f"No faces found in image2: {image_path2}")

    # Compare faces - note: we're comparing the first face found in each image
    match_results = face_recognition.compare_faces([face_encodings1[0]], face_encodings2[0])
    distance = face_recognition.face_distance([face_encodings1[0]], face_encodings2[0])[0]

    return match_results[0], distance


def main():
    # Load two images for comparison
    image_path1 = r"D:\BANGNA1\Devlog\Personal\WebVerifyGovernmentID\face_recognition_backend\face_recognition_flask\converted_front_image.png"
    image_path2 = r"D:\BANGNA1\Devlog\Personal\WebVerifyGovernmentID\face_recognition_backend\face_recognition_flask\converted_back_image.png"
    image = cv2.imread(image_path1)
    if image is None:
        raise ValueError(f"Could not load image from path: {image_path}")

        # Check if image is already in RGB format
    if len(image.shape) == 2:  # Grayscale image
        print("Converting grayscale image to RGB.")
        image = cv2.cvtColor(image, cv2.COLOR_GRAY2RGB)
    elif image.shape[2] == 4:  # Has an alpha channel (e.g., PNG with transparency)
        print("Converting image with alpha channel to RGB.")
        image = cv2.cvtColor(image, cv2.COLOR_BGRA2RGB)
    else:
        # Convert BGR (OpenCV default) to RGB
        image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)

    face_locations = face_recognition.face_locations(image)

    print(f"Detected {len(face_locations)} face(s) in the image.")


if __name__ == "__main__":
    main()
