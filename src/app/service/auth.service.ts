import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap, BehaviorSubject } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Usuario } from 'src/app/model/usuario';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = environment.apiUrl;
  private currentUserSubject: BehaviorSubject<Usuario | null>;
  public currentUser: Observable<Usuario | null>;

  constructor(private http: HttpClient) {
    const currentUserFromStorage = localStorage.getItem("currentUser");
    this.currentUserSubject = new BehaviorSubject<Usuario | null>(currentUserFromStorage ? JSON.parse(currentUserFromStorage) : null);
    this.currentUser = this.currentUserSubject.asObservable();
  }
  
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
      this.currentUserSubject.next(null);
      localStorage.clear();
      sessionStorage.clear();
      window.location.href="/home"; 
  }

  // Para fins de teste
  getUser(): Usuario | null {
    return this.currentUserSubject.value;
    }
  }
