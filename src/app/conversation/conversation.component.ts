import { Component, Input, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute, Route, Router } from '@angular/router';
import { Conversa } from '../model/conversa';
import { Usuario } from '../model/usuario';
import { Mensagem } from '../model/mensagem';
import { MensagemService } from '../mensagem.service';
import { AuthService } from '../auth.service';
import { ChatService } from '../chat.service';

@Component({
  selector: 'app-conversation',
  templateUrl: './conversation.component.html',
  styleUrls: ['./conversation.component.css']
})
export class ConversationComponent implements OnInit {
  @Input() conversa: Conversa;
  mensagens: Mensagem[] = [];
  novaMensagem: string = '';
  currentUser: Usuario;
  usuarioDest: Usuario;

  constructor(private authService: AuthService, private mensagemService: MensagemService, private chatService: ChatService,
     private route: ActivatedRoute, private router: Router, private cdr: ChangeDetectorRef){}
  
  ngOnInit(): void {
    this.authService.getUser().subscribe((user: Usuario) => {
      this.currentUser = user;
      console.log("Usuário atual:", this.currentUser);
      this.loadConversa();
    }, error => {
      console.error("Erro ao obter usuário", error);
    });
  }

  // Método que carrega a conversa (e as mensagens) 
  loadConversa(): void {
    const conversaId = this.route.snapshot.paramMap.get("id");
    if(conversaId) {
      this.chatService.getConversa(/*Uso do '+' pra converter string em number*/+conversaId).subscribe(
        (conversa: Conversa) => {
        this.conversa = conversa;
        this.usuarioDest = conversa.usuarioDest;
        this.loadMensagens();
        console.log("Conversa carregada", this.conversa);
        console.log("Usuário destino", this.usuarioDest);
    },
  error => {
    console.error("Erro ao carregar conversa", error);
  })
  
}}

  loadMensagens(): void {
    if(this.conversa && this.conversa.id && this.currentUser && this.usuarioDest){
    this.mensagemService.getMensagensByConversaId(this.conversa.id).subscribe((mensagens: Mensagem[]) => {
      this.mensagens = mensagens;
    }, error => {
      console.error("Erro ao carregar mensagens", error)
    });
  }}

  enviarMensagem(): void {
    if(this.novaMensagem.trim() && this.currentUser && this.conversa && this.usuarioDest) {
      const mensagem : Mensagem = {
        texto : this.novaMensagem,
        usuarioRemetente : this.currentUser,
        usuarioDestino : this.conversa.usuarioDest,
        conversa : this.conversa,
        vista : false,
      };
      // Adicionando Log para correção de falhas
      console.log("Enviando mensagem", mensagem);

      this.mensagemService.createMessage(this.conversa.id, mensagem).subscribe((novaMensagem: Mensagem) => {
        // Log 
        console.log("Mensagem enviada com sucesso:", novaMensagem);
        this.mensagens.push(novaMensagem);
        this.novaMensagem = '';
      },
      error => {
        console.error("Erro ao enviar mensagem", error);
      })
    } else {
      console.warn("Não foi possível enviar mensagens, dados obrigatórios faltando.")
    }
  }

  apagaMensagem(id: number): void {
    if(this.conversa){
    this.mensagemService.deleteMessage(this.conversa.id, id).subscribe(
      () => {
        this.mensagens = this.mensagens.filter(msg => msg.id !== id)
        // this.loadMensagens();
      }, error => {
        console.error("Erro ao deletar mensagem", error)
      }
    )
  }
}

  editaMensagem(mensagem: Mensagem): void {
    mensagem.editando = true;
  }

  salvaMensagemEditada(id: number | undefined): void {
    if(id !== undefined){
    const mensagemParaSalvar = this.mensagens.find(msg => msg.id === id);
    if(mensagemParaSalvar){
      this.atualizaMensagem(id, mensagemParaSalvar);
    }
    // Detecta estado de mudança de estado
    this.cdr.detectChanges();
  } else {
      console.error("O ID da mensagem não está definido.")
    }
  }
  
  
  atualizaMensagem(id: number, mensagem: Mensagem): void {
    if(this.conversa){
      // shallow copy
      const mensagemParaAtualizar = {...mensagem};
      delete mensagemParaAtualizar.editando;
      
      this.mensagemService.updateMessage(this.conversa.id, id, mensagemParaAtualizar).subscribe((mensagemAtualizada) => {
        const index = this.mensagens.findIndex(msg => msg.id === id);
        if(index !== -1){
          this.mensagens[index] = mensagemAtualizada;
          this.mensagens[index].editando = false;
          // Detecta estado de mudança de estado
          this.cdr.detectChanges();
        }
        // this.loadMensagens();
      }, error => {
        console.error("Erro ao atualizar mensagem", error);
      });
    }
  }

  marcaComoLida(id: number): void {
    if(this.conversa){
    this.mensagemService.markAsRead(this.conversa.id, id).subscribe(
      () => {
        const mensagem = this.mensagens.find(msg => msg.id === id);
        if(mensagem) {
          mensagem.vista = true;
        }
      },
      error => {
        console.error("Erro ao marcar mensagem como lida", error)
      }
    )}
  }

  marcaTodasComoLidas(): void {
    if(this.conversa){
    this.mensagemService.markAllAsRead(this.conversa.id).subscribe(
      () => {
        this.mensagens.forEach(msg => msg.vista = true)
      },
      error => {
        console.error("Erro ao marcar mensagens como lidas", error)
      }
    )}
  }

  voltarParaConversas(): void{
    this.router.navigate(['/chat'])
  }
}