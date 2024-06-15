import { Component, OnInit } from '@angular/core';
import { ChatService } from '../chat.service';
import { Conversa } from '../model/conversa';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {
  conversas: Conversa[] = [];
  novaConversa: Conversa = new Conversa();

  constructor(private chatService: ChatService, private authService: AuthService){}

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

  deleteConversa(id: number): void {
    this.chatService.deleteConversa(id).subscribe(
      () => {
        console.log("Conversa deletada com sucesso!")
        this.loadConversas();
      },
      error => {
        console.error("Erro ao deletar conversa", error);
      }
    );
  }

  startConversa(): void {
    this.chatService.startConversa(this.novaConversa).subscribe(
      (conversa: Conversa) => {
        console.log("Conversa iniciada", conversa);
        this.conversas.push(conversa);
        this.novaConversa = new Conversa();
      },
      error => {
        console.error("Erro ao iniciar conversa", error)
      }
    )
  };

  endConversa(id: number): void {
    this.chatService.endConversa(id).subscribe(
      response => {
        console.log(response);
        this.loadConversas();
      },
      error => {
        console.error("Erro ao encerrar conversas", error)
      }
    )
  };

  logout(): void {
    this.authService.logout();
  }
}

