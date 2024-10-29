import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CameraComponent } from './camera.component';
import { RouterModule } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { MatDialogModule } from '@angular/material/dialog';
import { JwtInterceptor } from '../jwt.interceptor';

@NgModule({
  declarations: [CameraComponent],
  imports: [
    CommonModule,
    MatDialogModule,
    RouterModule.forChild([{ path: '', component: CameraComponent }])
  ],
  providers: [
    provideHttpClient(
      withInterceptors([JwtInterceptor]) // Register the functional interceptor here
    ),
  ],
})
export class CameraModule { }
