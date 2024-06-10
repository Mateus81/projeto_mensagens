import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-cadastro',
  templateUrl: './cadastro.component.html',
  styleUrls: ['./cadastro.component.css']
})
export class CadastroComponent {
  nome: string = '';
  email: string = '';
  senha: string = '';

  constructor(private authService: AuthService, private router: Router){}

  onCadastro(): void {
    this.authService.cadastro(this.nome, this.email, this.senha).subscribe(
      response => {
        this.router.navigate(['/login']);
      },
      error => {
        console.error('Erro ao fazer cadastro', error);
        alert('Falha ao fazer cadastro. Tente novamente');
      }
    );
  }
}
