CREATE DATABASE WEB_VERIFY;

USE WEB_VERIFY;

CREATE TABLE USER (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    SoCMT VARCHAR(20) NOT NULL,
    HoVaTen VARCHAR(100) NOT NULL,
    NgaySinh DATE NOT NULL,
    GioiTinh ENUM('Nam', 'Nữ') NOT NULL,
    QuocTich VARCHAR(50) NOT NULL,
    QueQuan VARCHAR(255) NOT NULL,
    NoiThuongTru VARCHAR(255) NOT NULL,
    AnhCMT LONGBLOB -- Lưu file ảnh dưới dạng binary
);

-- Bảng lưu tài khoản fix sẵn
CREATE TABLE account (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    roleid BIGINT default 1
);
