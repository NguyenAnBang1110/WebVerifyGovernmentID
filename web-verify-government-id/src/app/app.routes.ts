import { Routes } from '@angular/router';
import { FormComponent } from './form/form.component';
import { UploadComponent } from './upload/upload.component';
import { CameraComponent } from './camera/camera.component';

export const routes: Routes = [
  { path: 'form', component: FormComponent },
  { path: 'upload', component: UploadComponent },
  { path: 'camera', component: CameraComponent },
  { path: '', redirectTo: '/form', pathMatch: 'full' } // Redirect đến form nếu đường dẫn rỗng
];
