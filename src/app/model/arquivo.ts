export class Arquivo {
    id: number;
    nome: string;
    tipo: string;
    tamanho: number;
    dataEnvio: Date;
    usuarioId: number; // Usuário que envia o arquivo
}