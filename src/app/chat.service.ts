import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Conversa } from './model/conversa';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  private apiUrl = environment.apiUrl; 

  constructor(private http: HttpClient) { }

  getConversas(): Observable<any[]> {
    return this.http.get<Conversa[]>(`${this.apiUrl}/conversas`);
  }

  getConversa(id: number): Observable<any> {
    return this.http.get<Conversa>(`${this.apiUrl}/conversas/${id}`);
  }

  deleteConversa(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/conversas/${id}`)
  }

  startConversa(conversa: Conversa): Observable<Conversa> {
    return this.http.post<Conversa>(`${this.apiUrl}/conversas`, conversa)
  }

  endConversa(id: number): Observable<string> {
    return this.http.put<string>(`${this.apiUrl}/conversas/${id}`, null)
  }
}
