import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UploadComponent } from './upload.component';
import { NgxFileDropModule } from 'ngx-file-drop';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [UploadComponent],
  imports: [
    CommonModule,
    NgxFileDropModule,
    RouterModule.forChild([{ path: '', component: UploadComponent }])
  ]
})
export class UploadModule { }
