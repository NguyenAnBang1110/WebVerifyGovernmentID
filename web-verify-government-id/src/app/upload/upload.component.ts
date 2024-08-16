import { Component } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { NgxFileDropModule, NgxFileDropEntry, FileSystemFileEntry, FileSystemDirectoryEntry } from 'ngx-file-drop';
import { API_URL } from '../constant';

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrl: './upload.component.scss'
})
export class UploadComponent {
  public files: NgxFileDropEntry[] = [];
  public uploadedFiles: string[] = [];

  public ocrText: any = '';
  public saveToFile: boolean = false; // Biến để kiểm soát việc lưu kết quả vào file

  constructor(private http: HttpClient) {}

  public dropped(files: NgxFileDropEntry[]) {
    this.files = files;
    for (const droppedFile of files) {
      if (droppedFile.fileEntry.isFile) {
        const fileEntry = droppedFile.fileEntry as FileSystemFileEntry;
        fileEntry.file((file: File) => {
          if (file.type.startsWith('image/')) {
            const reader = new FileReader();
            reader.onload = (e: any) => {
              this.uploadedFiles.push(e.target.result);
            };
            reader.readAsDataURL(file);
            this.uploadImage(file);
          } else {
            alert('Only image files are allowed!');
          }
        });
      } else {
        const fileEntry = droppedFile.fileEntry as FileSystemDirectoryEntry;
        console.log(droppedFile.relativePath, fileEntry);
      }
    }
  }

  public fileOver(event: any) {
    console.log(event);
  }

  public fileLeave(event: any) {
    console.log(event);
  }

  public uploadFile(event: any) {
    const file = event.target.files[0];
    if (file) {
      if (file.type.startsWith('image/')) {
        const reader = new FileReader();
        reader.onload = (e: any) => {
          this.uploadedFiles.push(e.target.result);
        };
        reader.readAsDataURL(file);
        this.uploadImage(file);
      } else {
        alert('Only image files are allowed!');
      }
    }
  }

  private uploadImage(file: File) {
    const formData: FormData = new FormData();
    formData.append('file', file, file.name);

    const headers = new HttpHeaders({
      'Authorization': 'Basic ' + btoa('admin:admin123') // Đổi username:password theo cấu hình của bạn
    });

    this.http.post(`${API_URL}/images/upload?saveToFile=` + this.saveToFile, formData, { headers: headers, responseType: 'text' })
      .subscribe(response => {
        debugger
        console.log('Upload successful', response);
        this.ocrText = response;
        console.log('OCR Result:', this.ocrText);
      }, error => {
        console.error('Upload failed', error);
      });
  }
}

