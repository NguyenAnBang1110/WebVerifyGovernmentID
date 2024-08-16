import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UploadComponent } from './upload.component';
import { NgxFileDropModule } from 'ngx-file-drop';
import { RouterModule } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

@NgModule({
  declarations: [UploadComponent],
  imports: [
    CommonModule,
    NgxFileDropModule,
    FormsModule,
    RouterModule.forChild([{ path: '', component: UploadComponent }])
  ],
  providers: [provideHttpClient()], // add it here
})
export class UploadModule { }
