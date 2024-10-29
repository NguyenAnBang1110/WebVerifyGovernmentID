import { Component, ViewChild, ElementRef } from '@angular/core';
import { API_URL } from '../constant';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-camera',
  templateUrl: './camera.component.html',
  styleUrl: './camera.component.scss'
})
export class CameraComponent {
  @ViewChild('frontVideo', { static: false }) frontVideo!: ElementRef<HTMLVideoElement>;
  @ViewChild('backVideo', { static: false }) backVideo!: ElementRef<HTMLVideoElement>;
  @ViewChild('frontCanvas') frontCanvas!: ElementRef;
  @ViewChild('backCanvas') backCanvas!: ElementRef;

  constructor(private http: HttpClient) { }

  ngOnInit() {
    if (typeof navigator !== 'undefined') {
      navigator.mediaDevices.getUserMedia({ video: true })
        .then(stream => {
          stream.getTracks().forEach(track => track.stop()); // Dừng stream để reset camera
          this.requestCamera();
        })
        .catch(() => {
          console.error('Permission denied');
        });
    }
  }

  requestCamera() {
    if (navigator && typeof navigator !== 'undefined') {
      navigator.mediaDevices.getUserMedia({ video: true })
        .then(stream => {
          this.frontVideo.nativeElement.srcObject = stream;
          this.backVideo.nativeElement.srcObject = stream;
        })
        .catch(err => {
          console.error('Camera not accessible:', err);
        });
    }
  }


  captureImage(side: string) {
    const canvas = side === 'front' ? this.frontCanvas.nativeElement : this.backCanvas.nativeElement;
    const video = side === 'front' ? this.frontVideo.nativeElement : this.backVideo.nativeElement;
    canvas.getContext('2d').drawImage(video, 0, 0, canvas.width, canvas.height);
  }

  confirm() {
    debugger
    const formData: FormData = new FormData();

    const frontBlob = this.dataURItoBlob(this.frontCanvas.nativeElement.toDataURL('image/png'));
    const backBlob = this.dataURItoBlob(this.backCanvas.nativeElement.toDataURL('image/png'));

    formData.append('frontImage', frontBlob, 'frontImage.png');
    formData.append('backImage', backBlob, 'backImage.png');

    this.http.post(`${API_URL}/face/compare`, formData)
      .subscribe(response => {
        debugger
        console.log('Face comparison result:', response);
      }, error => {
        console.error('Face comparison failed', error);
      });
  }

  dataURItoBlob(dataURI: string) {
    const byteString = atob(dataURI.split(',')[1]);
    const ab = new ArrayBuffer(byteString.length);
    const ia = new Uint8Array(ab);
    for (let i = 0; i < byteString.length; i++) {
      ia[i] = byteString.charCodeAt(i);
    }
    return new Blob([ab], { type: 'image/png' });
  }

}
