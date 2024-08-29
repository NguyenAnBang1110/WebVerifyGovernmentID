import cv2
import face_recognition
import numpy as np


def load_and_process_image(image_path):
    # Load the image from the given path
    image = cv2.imread(image_path)
    if image is None:
        raise Exception(f"Could not load image at path: {image_path}")

    # Convert image to RGB format (face_recognition expects RGB format)
    image_rgb = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)

    # Return the processed image
    return image_rgb


def detect_faces_with_opencv(image_rgb):
    # Using OpenCV's built-in face detector
    face_cascade = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_frontalface_default.xml')
    faces = face_cascade.detectMultiScale(image_rgb, scaleFactor=1.1, minNeighbors=5, minSize=(30, 30))

    # Draw rectangles around detected faces
    for (x, y, w, h) in faces:
        cv2.rectangle(image_rgb, (x, y), (x + w, y + h), (255, 0, 0), 2)

    return faces, image_rgb


def detect_faces_with_face_recognition(image_rgb):
    # Detect face locations using face_recognition
    try:
        face_locations = face_recognition.face_locations(image_rgb)
        if not face_locations:
            raise Exception("No faces detected")

        # Draw rectangles around detected faces
        for (top, right, bottom, left) in face_locations:
            cv2.rectangle(image_rgb, (left, top), (right, bottom), (255, 0, 0), 2)

        return face_locations, image_rgb
    except Exception as e:
        print(f"Face detection failed: {e}")
        return None, image_rgb


def save_image(image_rgb, output_path):
    # Convert back to BGR format for saving (since OpenCV saves in BGR)
    image_bgr = cv2.cvtColor(image_rgb, cv2.COLOR_RGB2BGR)
    cv2.imwrite(output_path, image_bgr)


def main():
    # Define the path to your image
    image_path = r"D:\BANGNA1\Devlog\Personal\WebVerifyGovernmentID\face_recognition_backend\face_recognition_flask\front_image.png"  # Update this with your image path
    output_path_opencv = r"D:\BANGNA1\Devlog\Personal\WebVerifyGovernmentID\face_recognition_backend\face_recognition_flask\output_opencv.png"
    output_path_facerec = r"D:\BANGNA1\Devlog\Personal\WebVerifyGovernmentID\face_recognition_backend\face_recognition_flask\output_facerec.png"

    # Load and process the image
    image_rgb = load_and_process_image(image_path)

    # Test case 1: Detect faces using OpenCV
    faces_opencv, image_with_faces_opencv = detect_faces_with_opencv(image_rgb.copy())
    save_image(image_with_faces_opencv, output_path_opencv)
    print(f"Detected {len(faces_opencv)} faces with OpenCV.")

    # Test case 2: Detect faces using face_recognition
    faces_facerec, image_with_faces_facerec = detect_faces_with_face_recognition(image_rgb.copy())
    if faces_facerec:
        save_image(image_with_faces_facerec, output_path_facerec)
        print(f"Detected {len(faces_facerec)} faces with face_recognition.")
    else:
        print("Face detection with face_recognition failed.")


if __name__ == "__main__":
    main()
