import cv2
import numpy as np
from skimage import feature

def extract_face_features(image):
    # Convert to grayscale
    gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)

    # Detect faces using Haar Cascades
    face_cascade = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_frontalface_default.xml')
    faces = face_cascade.detectMultiScale(gray, scaleFactor=1.1, minNeighbors=5, minSize=(30, 30))

    if len(faces) == 0:
        raise ValueError("No faces found in the image")

    x, y, w, h = faces[0]
    face = gray[y:y + h, x:x + w]

    # Resize face to a standard size (e.g., 100x100)
    face = cv2.resize(face, (100, 100))

    # Compute HOG features
    hog_features = feature.hog(face, orientations=9, pixels_per_cell=(8, 8),
                               cells_per_block=(2, 2), block_norm='L2-Hys')

    return hog_features


def compare_faces(image1, image2):
    features1 = extract_face_features(image1)
    features2 = extract_face_features(image2)

    # Compute distance between features (Euclidean distance)
    distance = np.linalg.norm(features1 - features2)

    # Define a threshold for similarity (this needs to be tuned based on your dataset)
    threshold = 0.3

    if distance < threshold:
        return True, distance
    else:
        return False, distance


def main():
    # Load two images for comparison
    image_path1 = r"D:\BANGNA1\Devlog\Personal\WebVerifyGovernmentID\face_recognition_backend\face_recognition_flask\converted_front_image.png"  # Thay thế bằng đường dẫn ảnh của bạn
    image_path2 = r"D:\BANGNA1\Devlog\Personal\WebVerifyGovernmentID\face_recognition_backend\face_recognition_flask\converted_back_image.png"  # Thay thế bằng đường dẫn ảnh của bạn

    image1 = cv2.imread(image_path1)
    image2 = cv2.imread(image_path2)

    if image1 is None or image2 is None:
        raise Exception("Could not load one of the images. Please check the paths.")

    # Compare the faces
    is_match, distance = compare_faces(image1, image2)

    print(f"Faces Match: {is_match}, Distance: {distance}")


if __name__ == "__main__":
    main()
