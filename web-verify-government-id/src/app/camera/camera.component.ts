import { Component, ViewChild, ElementRef } from '@angular/core';

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

  // ngAfterViewInit() {
  //   if (typeof navigator !== 'undefined' && navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
  //     navigator.mediaDevices.getUserMedia({ video: true })
  //       .then(stream => {
  //         this.frontVideo.nativeElement.srcObject = stream;
  //         this.backVideo.nativeElement.srcObject = stream;
  //       })
  //       .catch(err => {
  //         console.error('Camera not accessible: ', err);
  //       });
  //   } else {
  //     console.error('Navigator is not available in this environment.');
  //   }
  // }

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
    // Xử lý logic sau khi người dùng nhấn nút Xác Nhận
  }
}
