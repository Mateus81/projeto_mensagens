import { Conversa } from "./conversa";
import { Usuario } from "./usuario";

export class Mensagem {
    // ? - Opcional
    id?: number;
    texto: string;
    conversa: Conversa;
    usuarioDestino: Usuario;
    usuarioRemetente: Usuario;
    vista: boolean;
    // método para editar mensagem (opcional = ?)
    editando?: boolean = false;
    deletada?: boolean = false;

    constructor(){
        this.usuarioDestino = new Usuario();
        this.usuarioRemetente = new Usuario();
        this.conversa = new Conversa();
    }
}