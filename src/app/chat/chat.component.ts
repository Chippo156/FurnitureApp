import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ChatService } from '../service/websocket.service';
import { UserService } from '../service/user.service';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css'],
})
export class ChatComponent implements OnInit {
  chatForm: FormGroup;
  messages: any[] = [];
  constructor(
    private fb: FormBuilder,
    private chatService: ChatService,
    private userService: UserService
  ) {
    this.chatForm = this.fb.group({
      message: [''],
    });
  }
  userResponse = this.userService.getUserResponseFromLocalStorage();

  ngOnInit(): void {
    // Kết nối WebSocket
    this.chatService.connectWebSocket();

    // Lấy tin nhắn cũ
    this.getMessages();
  }

  // Gửi tin nhắn qua API
  sendMessage(): void {
    const message = {
      sender: this.userResponse?.id,
      receiver: 6,
      content: this.chatForm.value.message,
    };

    this.chatService.sendMessage(message).subscribe(() => {
      this.messages.push(message);
      this.chatForm.reset(); // Xóa nội dung trong input sau khi gửi
    });
  }

  // Lấy danh sách tin nhắn từ server
  getMessages(): void {
    this.chatService.getMessages().subscribe((data) => {
      this.messages = data;
    });
  }

  // Gửi tin nhắn qua WebSocket
  sendMessageThroughWebSocket(): void {
    const message = this.chatForm.value.message;
    this.chatService.sendMessageThroughWebSocket(message);
    this.chatForm.reset();
  }
}
