import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-no-permission',
  standalone: true,
  imports: [],
  templateUrl: './no-permission.component.html',
  styleUrl: './no-permission.component.scss'
})
export class NoPermissionComponent {

  constructor(private router: Router) {}

  goToLogin() {
    this.router.navigate(['/login']);
  }

}
