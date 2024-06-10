import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Conversa } from './chat/conversa';
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
}
