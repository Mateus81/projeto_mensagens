import { Expose, Transform } from "class-transformer";
import { Usuario } from "./usuario";
import { Buffer } from "buffer";

export class Contato {
    id?: number;
    nome: string;
    email: string;
    telefone?: string;
    usuario: Usuario;
    // @Lob e byte[] no back-end
    @Expose()
    @Transform(({value}) => value ? Buffer.from(value, 'base64') : null, {toClassOnly : true})
    @Transform(({value}) => value ? value.toString('base64') : null, {toPlainOnly : true})
    foto: Buffer | null;
}