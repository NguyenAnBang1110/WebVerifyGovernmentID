import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, RouterOutlet } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { API_URL } from '../constant';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  loginForm: FormGroup;
  showLoginForm: boolean = false;

  loginError: string = ''; // Biến để lưu trữ thông báo lỗi

  constructor(private fb: FormBuilder, private router: Router, private http: HttpClient,) {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  showLogin() {
    this.showLoginForm = true;
  }

  redirectToRegister() {
    this.router.navigate(['/register']); // Điều hướng đến màn hình đăng ký
  }

  onSubmit() {
    debugger
    if (this.loginForm.valid) {
      const loginData = this.loginForm.value;

      // Gọi API login
      this.http.post(`${API_URL}/auth/login`, loginData).subscribe({
        next: (response: any) => {
          const userToken = response.token;
          const refreshToken = response.refreshToken;

          // Lưu token và refresh token vào localStorage
          localStorage.setItem('userToken', userToken);
          localStorage.setItem('refreshToken', refreshToken);

          console.log("Login successful, token stored:", userToken);
          this.navigateBaseOnRoleID();
        },
        error: (error) => {
          console.error("Login failed:", error);

          // Hiển thị thông báo lỗi cho người dùng
          this.loginError = 'Tài khoản hoặc mật khẩu không đúng';
        }
      });
    } else {
      console.log("Form is invalid");
      this.loginError = 'Vui lòng điền đầy đủ thông tin đăng nhập'; // Thông báo nếu form không hợp lệ
    }
  }

  /**
   * Thực hiện navigate màn hình dựa trên phân quyền
   */
  navigateBaseOnRoleID() {
    this.router.navigate(['/upload']); // Điều hướng sau khi đăng nhập thành công
  }
}
