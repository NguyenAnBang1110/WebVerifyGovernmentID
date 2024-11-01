import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UploadComponent } from './upload.component';
import { NgxFileDropModule } from 'ngx-file-drop';
import { RouterModule } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { JwtInterceptor } from '../jwt.interceptor';

@NgModule({
  declarations: [UploadComponent],
  imports: [
    CommonModule,
    NgxFileDropModule,
    FormsModule,
    RouterModule.forChild([{ path: '', component: UploadComponent }])
  ],
  providers: [
    provideHttpClient(
      withInterceptors([JwtInterceptor]) // Register the functional interceptor here
    ),
  ],
})
export class UploadModule { }
