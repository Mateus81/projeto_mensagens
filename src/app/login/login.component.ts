import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth.service'; 
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  email: string = '';
  password: string = '';
  lembrar: boolean = false;

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    const rememberedUser = localStorage.getItem('rememberedUser');
    if(rememberedUser){
      const user = JSON.parse(rememberedUser);
      this.email = user.email;
      this.password = user.password;
      this.lembrar = true;
    }
  }

  onLogin(): void {
    this.authService.login(this.email, this.password).subscribe(
      response => {
        if(this.lembrar){
          localStorage.setItem('currentUser', JSON.stringify({email: this.email, password: this.password}));
        } else {
          localStorage.removeItem('rememberedUser');
        }
        localStorage.setItem('currentUser', JSON.stringify(response));
        this.router.navigate(['/chat']);
      },
      error => {
        console.error('Erro ao fazer login:', error);
        alert('Falha ao fazer login. Verifique suas credenciais.');
      }
    );
  }
}
