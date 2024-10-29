import { Routes } from '@angular/router';
import { FormComponent } from './form/form.component';
import { AuthGuard } from './auth.guard';
import { NoPermissionComponent } from './no-permission/no-permission.component';

export const routes: Routes = [
  { path: 'form', component: FormComponent, canActivate: [AuthGuard]  },
  { path: 'upload', loadChildren: () => import('./upload/upload.module').then(m => m.UploadModule), canActivate: [AuthGuard]  },
  { path: 'camera', loadChildren: () => import('./camera/camera.module').then(m => m.CameraModule), canActivate: [AuthGuard]  },
  { path: 'login', loadChildren: () => import('./login/login.module').then(m => m.LoginModule) },
  { path: 'no-permission', component: NoPermissionComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full' } // Redirect đến login nếu đường dẫn rỗng
];
