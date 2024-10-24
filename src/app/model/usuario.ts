import { Contato } from "./contato"
import { Conversa } from "./conversa";
import { Expose, Transform } from "class-transformer";
import { Buffer } from "buffer";

export class Usuario {
    id: number;
    nome: string;
    email: string;
    senha: string;
    conversas: Conversa[];
    contatos: Contato[];

    // Lob e Byte no back-end
    @Expose()
    @Transform(({ value }) => value ? Buffer.from(value, 'base64') : null, {toClassOnly : true})
    @Transform(({ value }) => value ? value.toString('base64') : null, {toPlainOnly : true})
    foto: Buffer | null;
}