import { Component, OnInit } from '@angular/core';
import { Conversa } from '../model/conversa';
import { Usuario } from '../model/usuario';
import { AuthService } from '../auth.service';
import { ChatService } from '../chat.service';
import { UserService } from '../user.service';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {
  conversas: Conversa[] = [];
  novaConversa: Conversa = new Conversa();
  usuario: Usuario = new Usuario();
  usuarioDest: Usuario = new Usuario();

  constructor(private chatService: ChatService, private authService: AuthService, private userService: UserService){}

  ngOnInit(): void {
    this.loadLoggedUser();
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

  loadLoggedUser(): void {
    const currentUserString = localStorage.getItem('currentUser');
    if(currentUserString){
      const currentUser = JSON.parse(currentUserString);
    if(currentUser && currentUser.id){
      this.userService.getUsuarioById(currentUser.id).subscribe(
        (user: Usuario) => {
          this.usuario = user;
        },
        error => {
          console.error("Erro ao carregar usuário logado", error);
        }
      );
    } else {
      console.error("Usuário logado não encontrado");
    }}
  }

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
    if(this.usuario && this.usuario.nome && this.usuarioDest && this.usuarioDest.nome){
    this.userService.getUsuarioByNome(this.usuarioDest.nome).subscribe(
      (destinatario: Usuario) => {
        if(destinatario){
          const novaConversa = new Conversa();
          novaConversa.usuario = this.usuario; // usuário inicial
          novaConversa.usuarioDest = destinatario; // usuário destino

          console.log("Usuário destino: ", destinatario); // log
   
    this.chatService.startConversa(novaConversa).subscribe(
      (conversa: Conversa) => {
        console.log("Conversa iniciada", conversa);
        this.conversas.push(conversa);
        this.novaConversa = new Conversa();
        this.usuarioDest = new Usuario();
      },
      error => {
        console.error("Erro ao iniciar conversa", error);
      }
    );
  }
},
    error => {
      console.error("Erro ao buscar usuário destinatário", error);
    }
  );
  }
}

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

