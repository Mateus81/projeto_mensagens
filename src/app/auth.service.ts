import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient, private router: Router) {}
  
  login(email: string, senha: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/usuarios/login`, { email, senha }, {withCredentials: true});
  }
  
  cadastro(nome: string, email: string, senha: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/usuarios`, {nome, email, senha});
  }

  logout(): void {
    this.http.delete(`${this.apiUrl}/logout`, {}).subscribe(() => {
      this.router.navigate(['/home']);
    }, error => {
        console.error("Erro ao sair", error);
    });
  }

  // Para fins de teste
  getUser() {
    return this.http.get(`${this.apiUrl}/user`)
  }
}
