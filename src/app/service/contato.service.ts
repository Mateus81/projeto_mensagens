import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable, tap } from "rxjs";
import { environment } from "src/environments/environment";
import { Contato } from "../model/contato";
import { AuthService } from "./auth.service";

@Injectable({
    providedIn: "root"
})

export class ContatoService {

    private apiUrl = environment.apiUrl;

    constructor(private http: HttpClient, private authService: AuthService){}

    getContato(id: number): Observable<Contato>{
        return this.http.get<Contato>(`${this.apiUrl}/contatos/${id}`, {withCredentials: true});
    }

    getContatos(): Observable<Contato[]>{
        const currentUser = this.authService.getUser();
        if(!currentUser){
            throw new Error("Usuário não autenticado");
        }
        return this.http.get<Contato[]>(`${this.apiUrl}/contatos`, {withCredentials: true}).pipe(tap(
            data => console.log("Dados recebidos do back-end", data)));
    }

    deleteContato(id: number): Observable<void>{
        const currentUser = this.authService.getUser();
        if(!currentUser){
            throw new Error("Usuário não autenticado");
        }
        return this.http.delete<void>(`${this.apiUrl}/contatos/${id}`, {withCredentials: true})
    }

    insertContato(contato: Contato): Observable<Contato> {
        return this.http.post<Contato>(`${this.apiUrl}/contatos/`, contato, {withCredentials: true});
    }

    updateContato(id: number, contato: Contato): Observable<Contato> {
        return this.http.put<Contato>(`${this.apiUrl}/contatos/${id}`, contato, {withCredentials: true});
    }

}