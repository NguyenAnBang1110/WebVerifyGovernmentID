import { Routes } from '@angular/router';
import { FormComponent } from './form/form.component';

export const routes: Routes = [
  { path: 'form', component: FormComponent },
  { path: 'upload', loadChildren: () => import('./upload/upload.module').then(m => m.UploadModule) },
  { path: 'camera', loadChildren: () => import('./camera/camera.module').then(m => m.CameraModule) },
  { path: '', redirectTo: '/form', pathMatch: 'full' } // Redirect đến form nếu đường dẫn rỗng
];
