import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FormComponent } from './form/form.component';
import { UploadComponent } from './upload/upload.component';
import { CameraComponent } from './camera/camera.component';
import { NgxFileDropModule } from 'ngx-file-drop';

@NgModule({
  declarations: [
    AppComponent,
    FormComponent,
    UploadComponent,
    CameraComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgxFileDropModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
