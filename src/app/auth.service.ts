import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap, BehaviorSubject } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Router } from '@angular/router';
import { Usuario } from './model/usuario';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = environment.apiUrl;
  private currentUserSubject: BehaviorSubject<Usuario | null> = new BehaviorSubject<Usuario | null>(null);
  public currentUser: Observable<Usuario | null> = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient, private router: Router) {}
  
  login(email: string, senha: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/usuarios/login`, { email, senha }, {withCredentials: true}).pipe(
      tap(user => {
        this.currentUserSubject.next(user);
        localStorage.setItem('currentUser', JSON.stringify(user))
      })
    );
  }
  
  cadastro(nome: string, email: string, senha: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/usuarios`, {nome, email, senha});
  }

  logout(): void {
    this.http.post(`${this.apiUrl}/logout`, {withCredentials: true}).subscribe(() => {
      this.currentUserSubject.next(null);
    //  localStorage.removeItem('currentUser');
      localStorage.clear();
      sessionStorage.clear();
      document.cookie.split(";").forEach(function(c) { 
        document.cookie = c.trim().split("=")[0] + 
        "=;expires=Thu, 01 Jan 1970 00:00:00 UTC;path=/";  
    });
      window.location.href="/home"; 
    },
      error => {
        console.error("Erro ao sair", error);
    });
  }

  // Para fins de teste
  getUser(): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.apiUrl}/user`, {withCredentials : true}).pipe(tap(user => {
      this.currentUserSubject.next(user);
      localStorage.setItem('currentUser', JSON.stringify(user));
    }))
  }
}
