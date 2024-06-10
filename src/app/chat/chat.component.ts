import { Component, OnInit } from '@angular/core';
import { ChatService } from '../chat.service';
import { Conversa } from './conversa';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {
  conversas: Conversa[] = [];
  conversa!: Conversa;

  constructor(private chatService: ChatService){}

  ngOnInit(): void {
    this.loadConversas();
  }

  loadConversas(): void {
    this.chatService.getConversas().subscribe(
      (data: Conversa[]) => {
        this.conversas = data;
      },
      (error) => {
        console.error('Erro ao carregar as conversas', error);
      }
    )
  };
}
