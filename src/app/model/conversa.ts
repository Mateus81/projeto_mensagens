import { Mensagem } from "./mensagem";
import { Usuario } from "./usuario";

export class Conversa {
    id: number;
    usuario: Usuario;
    usuarioDest: Usuario;
    mensagens: Mensagem[];

    constructor() {
      this.usuario = new Usuario();
      this.usuarioDest = new Usuario();
    }
}