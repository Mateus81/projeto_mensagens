import { Component, OnInit } from '@angular/core';
import { ArquivoService } from '../service/arquivo.service';
import { AuthService } from '../service/auth.service';
import { Arquivo } from '../model/arquivo';
import { Usuario } from '../model/usuario';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'app-arquivo',
  templateUrl: './arquivo.component.html',
  styleUrls: ['./arquivo.component.css']
})
export class ArquivoComponent implements OnInit {
  usuario: Usuario | null = null;
  arquivos: Arquivo[] = [];
  selectedFile: File | null = null;
  conversaId: number | null = null;
  isUploading: boolean = false;

  constructor(private arquivoService: ArquivoService, private authService: AuthService, private route: ActivatedRoute){}

  ngOnInit(): void {
    this.conversaId = +this.route.snapshot.params['id']; // '+' converte em número
    console.log("ID da conversa capturada: " + this.conversaId);
    this.usuario = this.authService.getUser();
    if(this.usuario){
      this.loadArquivos();
    }
  }

  loadArquivos(): void {
    this.arquivoService.getArquivos().subscribe(
      (data) => (this.arquivos = data),
      (error) => console.error("Erro ao carregar arquivos", error));
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if(input.files && input.files.length > 0){
      this.selectedFile = input.files[0];
    }
  }

  uploadArquivo(): void {
    if(this.selectedFile && this.conversaId){
      this.isUploading = true;
      this.arquivoService.uploadArquivo(this.conversaId, this.selectedFile).subscribe(
        (response) => {
          this.isUploading = false;
          console.log("Upload realizado com sucesso", response);
          this.loadArquivos(); // Atualiza a lista de arquivos
          this.selectedFile = null; // Limpa a seleção de arquivo
        },
        error => { 
          this.isUploading = false;  
          console.error("Erro ao fazer upload", error)});
    }
  }

  deleteArquivo(id: number): void {
    if(confirm("Tem certeza que deseja excluir esse arquivo?")){
      this.arquivoService.deleteArquivo(id).subscribe(
        () => {
          console.log(`Arquivo com ID ${id} excluído com sucesso`);
          this.loadArquivos(); // Atualiza a lista após excluir
        },
        (error) => console.error("Não foi possível excluir o arquivo", error));
    }
  }

  downloadArquivo(id: number): void {
    this.arquivoService.downloadArquivo(id).subscribe((response: HttpResponse<Blob>) => {
      const contentDisposition = response.headers.get('Content-Disposition');
      const fileNameMatch = contentDisposition?.match(/filename="(.+)"/);
      const filename = fileNameMatch ? fileNameMatch[1] : `arquivo_${id}`;

      const blob = new Blob([response.body!], { type: response.body?.type })
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = filename;
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
      window.URL.revokeObjectURL(url);
    }, (error) => console.error("Não foi possível baixar arquivo", error));
  }
}
