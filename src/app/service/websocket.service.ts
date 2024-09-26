import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ChatService {
  private baseUrl = 'http://localhost:8088/api/v1/chat'; // URL API backend của Spring Boot
  private webSocket!: WebSocket;

  constructor(private http: HttpClient) {}

  // Gửi tin nhắn
  sendMessage(message: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/sendMessage`, message);
  }

  // Lấy danh sách tin nhắn
  getMessages(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/messages`);
  }

  // Kết nối WebSocket
  connectWebSocket() {
    this.webSocket = new WebSocket('ws://localhost:8088/api/v1/chat');
    this.webSocket.onmessage = (event) => {
      console.log('Received from WebSocket:', event.data);
    };
  }

  // Gửi tin nhắn qua WebSocket
  sendMessageThroughWebSocket(message: string) {
    this.webSocket.send(message);
  }
}
