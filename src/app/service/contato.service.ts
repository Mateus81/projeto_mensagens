import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { Contato } from "../model/contato";

@Injectable({
    providedIn: "root"
})

export class ContatoService {

    private apiUrl = environment.apiUrl;

    constructor(private http: HttpClient){}

    // Path Variable {id}
    getContato(id: number): Observable<Contato>{
        return this.http.get<Contato>(`${this.apiUrl}/contatos/${id}`, {withCredentials: true});
    }

    // RequestParam
    getContatos(usuarioId: number): Observable<Contato[]>{
        return this.http.get<Contato[]>(`${this.apiUrl}/contatos?usuarioId=${usuarioId}`, {withCredentials: true});
    }

    deleteContato(id: number): Observable<void>{
        return this.http.delete<void>(`${this.apiUrl}/contatos/${id}`, {withCredentials: true})
    }

    insertContato(usuarioId: number, contato: Contato): Observable<Contato> {
        return this.http.post<Contato>(`${this.apiUrl}/contatos/${usuarioId}`, contato, {withCredentials: true});
    }

    updateContato(id: number, contato: Contato): Observable<Contato> {
        return this.http.put<Contato>(`${this.apiUrl}/contatos/${id}`, contato, {withCredentials: true});
    }

}