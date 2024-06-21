import { Usuario } from "./usuario";

export class Conversa {
    id: number;
    usuario: Usuario;

    constructor() {
      this.usuario = new Usuario();
    }
}