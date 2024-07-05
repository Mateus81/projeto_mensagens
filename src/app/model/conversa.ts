import { Usuario } from "./usuario";

export class Conversa {
    id: number;
    usuario: Usuario;
    usuarioDest: Usuario;

    constructor() {
      this.usuario = new Usuario();
      this.usuarioDest = new Usuario();
    }
}