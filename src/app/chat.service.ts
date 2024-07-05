import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Conversa } from './model/conversa';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  private apiUrl = environment.apiUrl; 

  constructor(private http: HttpClient) { }

  getConversas(): Observable<Conversa[]> {
    return this.http.get<Conversa[]>(`${this.apiUrl}/conversas`,  {withCredentials: true});
  }

  getConversa(id: number): Observable<Conversa> {
    return this.http.get<Conversa>(`${this.apiUrl}/conversas/${id}`,  {withCredentials: true});
  }

  deleteConversa(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/conversas/${id}`,  {withCredentials: true});
  }

  startConversa(conversa: Conversa): Observable<Conversa> {
    return this.http.post<Conversa>(`${this.apiUrl}/conversas`, conversa,  {withCredentials: true});
  }

  endConversa(id: number): Observable<string> {
    return this.http.put<string>(`${this.apiUrl}/conversas/${id}`, {},  {withCredentials: true});
  }
}
