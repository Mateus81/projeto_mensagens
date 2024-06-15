import { Conversa } from "./conversa";

export class Usuario {
    id: number;
    nome: string;
    email: string;
    senha: string;
    conversas: Conversa[];
}