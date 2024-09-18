import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AlertService {
  private alertSubject = new Subject<{ title: string; message: string }>();

  constructor() {}

  getAlert() {
    return this.alertSubject.asObservable();
  }

  showAlert(title: string, message: string) {
    debugger;
    this.alertSubject.next({ title, message });
  }
}
