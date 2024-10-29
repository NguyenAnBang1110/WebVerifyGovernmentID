import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginComponent } from './login.component';
import { NgxFileDropModule } from 'ngx-file-drop';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { MatDialogModule } from '@angular/material/dialog';
import { JwtInterceptor } from '../jwt.interceptor';


@NgModule({
  declarations: [LoginComponent],
  imports: [
    CommonModule,
    NgxFileDropModule,
    FormsModule,
    MatDialogModule,
    ReactiveFormsModule,
    RouterModule.forChild([{ path: '', component: LoginComponent }])
  ],
  providers: [
    provideHttpClient(
      withInterceptors([JwtInterceptor]) // Register the functional interceptor here
    ),
  ],
})
export class LoginModule { }
