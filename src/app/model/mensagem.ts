import { Conversa } from "./conversa";
import { Usuario } from "./usuario";

export class Mensagem {
    // Uso do "?" = id pode ser undefined
    id?: number;
    texto: string;
    conversa: Conversa;
    usuarioDestino: Usuario;
    usuarioRemetente: Usuario;
    vista: boolean;
}