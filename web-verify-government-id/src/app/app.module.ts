import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FormComponent } from './form/form.component';
import { UploadComponent } from './upload/upload.component';
import { CameraComponent } from './camera/camera.component';
import { NgxFileDropModule } from 'ngx-file-drop';
import { CommonModule } from '@angular/common';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { RouterModule } from '@angular/router';

// Import the JwtInterceptor
import { JwtInterceptor } from './jwt.interceptor';

@NgModule({
  declarations: [
    AppComponent,
    FormComponent,
    UploadComponent,
    CameraComponent,
  ],
  imports: [
    BrowserModule,
    CommonModule,
    NgxFileDropModule,
    AppRoutingModule, 
    RouterModule,
  ],
  providers: [
    provideHttpClient(
      withInterceptors([JwtInterceptor]) // Register the functional interceptor here
    ),
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  bootstrap: [AppComponent],
})
export class AppModule { }
