import face_recognition
import cv2
import numpy as np
import dlib
from PIL import Image



def main():
    print(dlib.__version__)
    image = Image.open("C:/Users/Admin/Downloads/IMG_4438.JPG")
    image.save("C:/Users/Admin/Downloads/IMG_4438_no_exif.JPG")
    image = face_recognition.load_image_file("C:/Users/Admin/Downloads/IMG_4438_no_exif.JPG")
    rgb_image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
    print(rgb_image.dtype)
    # face_locations = face_recognition.face_locations(rgb_image)


if __name__ == "__main__":
    main()
