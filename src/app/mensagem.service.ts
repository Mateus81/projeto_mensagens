import { Injectable } from '@angular/core';
import { Mensagem } from './model/mensagem';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})

export class MensagemService {

  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getMensagensByConversaId(conversaId: number): Observable<Mensagem[]> {
    return this.http.get<Mensagem[]>(`${this.apiUrl}/conversas/${conversaId}/mensagens`);
  }

  getMensagem(conversaId: number, id: number): Observable<Mensagem> {
    return this.http.get<Mensagem>(`${this.apiUrl}/conversas/${conversaId}/mensagens/${id}`);
  }

  createMessage(conversaId: number, mensagem: Mensagem): Observable<Mensagem> {
    return this.http.post<Mensagem>(`${this.apiUrl}/conversas/${conversaId}/mensagens`, mensagem);
  }

  deleteMessage(conversaId: number, id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/conversas/${conversaId}/mensagens/${id}`);
  }

  updateMessage(conversaId: number, id: number, mensagem: Mensagem): Observable<Mensagem> {
    return this.http.put<Mensagem>(`${this.apiUrl}/conversas/${conversaId}/mensagens/${id}`, mensagem)
  }

  markAsRead(conversaId: number, id: number): Observable<string> {
    return this.http.patch<string>(`${this.apiUrl}/conversas/${conversaId}/mensagens/${id}`, null)
  }

  markAllAsRead(conversaId: number): Observable<string> {
    return this.http.patch<string>(`${this.apiUrl}/conversas/${conversaId}/mensagens`, null)
  }
}
