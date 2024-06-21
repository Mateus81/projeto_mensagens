import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { Usuario } from "./model/usuario";
import { environment } from "src/environments/environment";

@Injectable({
    providedIn: 'root'
})

export class UserService {
    private apiUrl = environment.apiUrl; 

    constructor(private http: HttpClient){}

    getUsuarioByNome(nome: String): Observable<Usuario> {
        return this.http.get<Usuario>(`${this.apiUrl}/usuarios/nome/${nome}`)
    }

    getUsuarioById(id: number): Observable<Usuario> {
        return this.http.get<Usuario>(`${this.apiUrl}/usuarios/${id}`)
    }
}

