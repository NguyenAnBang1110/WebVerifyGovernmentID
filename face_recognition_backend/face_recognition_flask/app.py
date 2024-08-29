from flask import Flask, request, jsonify
import face_recognition
import base64
import cv2
import numpy as np

app = Flask(__name__)


@app.route('/')
def hello_world():  # put application's code here
    return 'Hello World!'


@app.route('/api/test', methods=['GET'])
def test_route():
    return "Test route is working!"


@app.route('/api/face/compare', methods=['POST'])
def compare_faces():
    data = request.json

    # Decode the base64 encoded images
    front_image_data = base64.b64decode(data['frontImage'])
    back_image_data = base64.b64decode(data['backImage'])

    # Convert the byte data to numpy arrays
    front_image = np.frombuffer(front_image_data, np.uint8)
    back_image = np.frombuffer(back_image_data, np.uint8)

    # Decode the numpy arrays into images
    front_image = cv2.imdecode(front_image, cv2.IMREAD_COLOR)
    back_image = cv2.imdecode(back_image, cv2.IMREAD_COLOR)

    # Convert images to RGB (even if they are already in RGB format)
    front_image = cv2.cvtColor(front_image, cv2.COLOR_BGR2RGB)
    back_image = cv2.cvtColor(back_image, cv2.COLOR_BGR2RGB)

    # Lưu lại hình ảnh sau khi chuyển đổi sang RGB
    cv2.imwrite('converted_front_image.png', front_image)
    cv2.imwrite('converted_back_image.png', back_image)

    # In ra loại hình ảnh để kiểm tra
    print(f"Front image shape: {front_image.shape}, dtype: {front_image.dtype}")
    print(f"Back image shape: {back_image.shape}, dtype: {back_image.dtype}")

    # Verify the images are decoded correctly
    if front_image is None or back_image is None:
        return jsonify({"error": "Image decoding failed"}), 400

    # Step 1: Detect face locations
    # Chuyển đổi hình ảnh sang RGB (nếu cần)
    rgb_image = front_image[:, :, ::-1]

    try:
        print(f"Start face_locations_front_TestRGB")
        face_locations_front_TestRGB = face_recognition.face_locations(rgb_image)
        print(f"Pass face_locations_front_TestRGB")
        face_locations_front = face_recognition.face_locations(front_image)
        face_locations_back = face_recognition.face_locations(back_image)
    except Exception as e:
        return jsonify({"error": f"Face detection failed: {str(e)}"}), 500


    # Step 2: Create face encodings from the detected locations
    try:
        front_encoding = face_recognition.face_encodings(front_image, face_locations_front)[0]
        back_encoding = face_recognition.face_encodings(back_image, face_locations_back)[0]
    except IndexError:
        return jsonify({"error": "No face detected in one or both images"}), 400

    # Step 3: Compare faces
    result = face_recognition.compare_faces([front_encoding], back_encoding)[0]

    return jsonify(match=result)


if __name__ == '__main__':
    app.run(port=5001, debug=True)

