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
    } else {
      console.error("Usuário não autenticado. Impossível carregar contatos");
    }
  }

  loadContatos(): void {
    if(!this.usuario){
      console.error("Usuário indefinido. Impossível carregar contatos");
      return; 
    }
      this.contatoService.getContatos().subscribe((
        contatos: Contato[]) => {
          this.contatos = contatos;
          console.log("Contatos carregados: ", contatos);
        }, error => {
          console.error("Erro ao carregar contatos", error);
        })
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
      console.error("Contato inexistente");
    }
  }

  addContato(): void {
    if(!this.usuario && !this.usuario!.id) {
      console.error("Usuário associado não foi encontrado");
      return;
    }

    if(!this.usuarioAdicionado.nome || !this.usuarioAdicionado.nome.trim()){
      console.error("Nome do contato não pode ser vazio");
      return;
    }

    this.userService.getUsuarioByNome(this.usuarioAdicionado.nome).subscribe((usuario: Usuario) => {
      if(!usuario){
        console.error("Usuário não encontrado");
        return;
      }

      const contatoExistente = this.contatos.find(contato => contato.email === usuario.email);
      if(contatoExistente){
        console.warn("Contato já adicionado");
        return;
      }

      const novoContato: Contato = {
        nome: usuario.nome,
        email: usuario.email,
        usuario: this.usuario as Usuario,
        foto: null,
      };
      this.contatoService.insertContato(novoContato).subscribe(() => {
        console.log("Contato adicionado com sucesso");
        this.usuarioAdicionado.nome = '';
        this.loadContatos();
        this.contato = null;
      }, 
      error => {
        console.error("Erro ao adicionar contato", error);
      });
    },
    error => {
      console.error("Erro ao buscar usuário pelo nome", error);
    });
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

  // Método que adiciona uma conversa com o contato caso ele não tenha uma com o usuário || Continua conversa com contato
  irParaConversa(id: number): void {
    console.log("Usuário atual em irParaConversa:", this.usuario);
    if(this.contato && this.usuario){
      // Verifica se existe conversa com o contato
      this.chatService.getConversas().subscribe((conversas: Conversa[])=> {
        const conversaExistente = conversas.find(c => c.usuarioDest.email === this.contato?.email || c.usuario.email === this.contato?.email);
        if(conversaExistente){
          this.router.navigate([`conversa/${conversaExistente.id}`]);
      } else {
        // Senão existe cria uma e navega até ela
        this.userService.getUsuarioByEmail(this.contato?.email!).subscribe((usuarioDest: Usuario) => {
          if(usuarioDest){
            const novaConversa = new Conversa();
            novaConversa.usuario = this.usuario as Usuario;
            novaConversa.usuarioDest = usuarioDest;
            novaConversa.status = "OPEN";

            this.chatService.startConversa(novaConversa).subscribe((conversaCriada: Conversa) => {
              this.chatService.getConversas().subscribe(atualizadas => {
                console.log("Lista de conversas atualizadas", atualizadas);
              })
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
