package com.signature.backend.util;

public final class Constants {

    //Định nghĩa quyền người dùng đăng nhập
    public interface ROLE {

        //Khách đăng ký thường
        int PERSONAL = 1;

        //An ninh
        int SECURITY = 2;

        //Hỗ trợ
        int SUPPORT = 3;

        //admin
        int ADMIN = 4;
    }
}
