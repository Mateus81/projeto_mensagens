import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../service/auth.service';
import { ContatoService } from '../service/contato.service'
import { UserService } from '../service/user.service';
import { ChatService } from '../service/chat.service';
import { Usuario } from '../model/usuario';
import { Contato } from '../model/contato';
import { Conversa } from '../model/conversa';

@Component({
  selector: 'app-contato',
  templateUrl: './contato.component.html',
  styleUrls: ['./contato.component.css']
})
export class ContatoComponent implements OnInit {
  usuario: Usuario | null;
  contato: Contato | null;
  contatos: Contato[] = [];
  conversa: Conversa;
  usuarioAdicionado: Usuario = new Usuario();
  // Atributos de edição
  editandoContato: boolean = false;
  contatoEditado: Contato;

  constructor(private authService: AuthService, private contatoService: ContatoService, 
    private userService: UserService, private router: Router, private chatService: ChatService){}

  ngOnInit(): void {
    this.usuario = this.authService.getUser();
    if(this.usuario){
      this.loadContatos();
    }
  }

  loadContatos(): void {
    if(this.usuario){
      this.contatoService.getContatos(this.usuario.id).subscribe((
        contatos: Contato[]) => {
          this.contatos = contatos;
        })
    } else {
      console.error("Não foi possível carregar sua lista de contatos");
    }
  }

  loadContato(id: number): void {
    if(this.usuario && this.contatos) {
      this.contatoService.getContato(id).subscribe(
        (contato: Contato) => {
          this.contato = contato;
        }, error => {
          console.error("Erro ao carregar contato", error);
        })
    } else {
      console.error("Contato inexistente")
    }
  }

  // Melhorar lógica de adicionar contato
  addContato(): void {
    if(this.usuario && this.usuarioAdicionado.nome && this.usuarioAdicionado.nome.trim()) {
      if(this.usuario){
      this.userService.getUsuarioByNome(this.usuarioAdicionado.nome).subscribe((usuario: Usuario) => {
        if(!usuario){
          console.error("Usuário não encontrado");
          return;
        }
        if(this.usuario?.id){
        const novoContato: Contato = {
          nome: usuario.nome,
          email: usuario.email,
          usuario: this.usuario as Usuario,
          foto: null
        };
        this.contatoService.insertContato(this.usuario.id, novoContato).subscribe(() => {
          console.log("Contato adicionado com sucesso");
          this.usuarioAdicionado.nome = '';
          this.loadContatos();
          this.contato = null;
        }, 
        error => {
          console.error("Erro ao adicionar contato", error);
        });
      } else {
        console.error("Usuário associado não foi encontrado");
      }}, 
      error => {
        console.error("Erro ao buscar usuário pelo nome", error);
      });
    } else {
      console.error("Nome do contato não pode ser vazio");
    }}
  }

  deleteContato(id: number): void {
    if(this.usuario && this.contato){
      this.contatoService.deleteContato(id).subscribe(() => {
        console.log("Contato deletado com sucesso");
        // Remove contato da lista local
        this.contatos = this.contatos.filter(c => c.id !== id);
        // Verifica se contato estava selecionado e limpa a sessão
        if(this.contato && this.contato.id === id){
          this.contato = null;
        }
      });
    } else {
      console.error("Não foi possível deletar contato");
    }
  }

  editarContato(contato: Contato): void {
    this.editandoContato = true;
    // Cria uma cópia para edição
    this.contatoEditado = { ...contato };
  }

  salvarEdicao(): void {
    if(this.contatoEditado && this.contatoEditado.nome.trim()){
      this.contatoService.updateContato(this.contatoEditado.id!, this.contatoEditado).subscribe(() => {
        console.log("Contato atualizado com sucesso!");
        const index = this.contatos.findIndex(c => c.id === this.contatoEditado.id);
        if(index > -1){
          this.contatos[index] = this.contatoEditado;
        }
        this.editandoContato = false;
      }, error => {
        console.error("Erro ao salvar a edição do contato", error);
      }
    )} else {
      console.error("O nome do contato não pode ser vazio");
    };
  }

  cancelaEdicao(): void {
    this.editandoContato = false;
  }

  voltarParaChat(): void {
    this.router.navigate(['/chat'])
  }

  irParaConversa(id: number): void {
    if(this.contato){
      // Verifica se existe conversa com o contato
      this.chatService.getConversas().subscribe((conversas: Conversa[])=> {
        const conversaExistente = conversas.find(c => c.usuarioDest.nome === this.contato?.nome || c.usuario.nome === this.contato?.nome);
        if(conversaExistente){
          this.router.navigate([`conversa/${conversaExistente.id}`]);
      } else {
        // Senão existe cria uma e navega até ela
        this.userService.getUsuarioByNome(this.contato?.nome!).subscribe((usuarioDest: Usuario) => {
          if(usuarioDest){
            const novaConversa = new Conversa();
            novaConversa.usuario = this.usuario as Usuario;
            novaConversa.usuarioDest = usuarioDest;

            this.chatService.startConversa(novaConversa).subscribe((conversaCriada: Conversa) => {
              this.router.navigate([`conversa/${conversaCriada.id}`]);
              console.log("Conversa criada com sucesso", conversaCriada);
            }, error => {
              console.error("Erro ao criar nova conversa", error);
            });
          }
        }, error => {
          console.error("Erro ao buscar usuário destino", error);
        });
      }
    }, error => {
      console.error("Erro ao buscar conversas", error);
    });
   }
  }

  logout(): void {
    this.authService.logout();
    this.usuario = null;
  }
}
