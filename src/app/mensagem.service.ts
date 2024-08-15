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
    return this.http.get<Mensagem[]>(`${this.apiUrl}/conversas/${conversaId}/mensagens`, {withCredentials: true});
  }

  getMensagem(conversaId: number, id: number): Observable<Mensagem> {
    return this.http.get<Mensagem>(`${this.apiUrl}/conversas/${conversaId}/mensagens/${id}`, {withCredentials: true});
  }

  createMessage(conversaId: number, mensagem: Mensagem): Observable<Mensagem> {
    return this.http.post<Mensagem>(`${this.apiUrl}/conversas/${conversaId}/mensagens`, mensagem, {withCredentials: true});
  }

  deleteMessage(conversaId: number, id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/conversas/${conversaId}/mensagens/${id}`, {withCredentials: true});
  }

  updateMessage(conversaId: number, id: number, mensagem: Mensagem): Observable<Mensagem> {
    console.log("Chamando serviço de atualização:", mensagem);
    return this.http.put<Mensagem>(`${this.apiUrl}/conversas/${conversaId}/mensagens/${id}`, mensagem, {withCredentials: true})
  }

  markAsRead(conversaId: number, id: number): Observable<string> {
    return this.http.patch<string>(`${this.apiUrl}/conversas/${conversaId}/mensagens/${id}`, null, {withCredentials: true})
  }

  markAllAsRead(conversaId: number): Observable<string> {
    return this.http.patch<string>(`${this.apiUrl}/conversas/${conversaId}/mensagens`, null, {withCredentials: true})
  }
}
