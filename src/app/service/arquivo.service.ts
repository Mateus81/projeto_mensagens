import { Injectable } from "@angular/core";
import { HttpClient, HttpResponse } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { Arquivo } from "../model/arquivo";

@Injectable({
    providedIn: 'root',
})

export class ArquivoService {
    private apiUrl = environment.apiUrl;

    constructor(private http: HttpClient){}

    getArquivos(): Observable<Arquivo[]>{
        return this.http.get<Arquivo[]>(`${this.apiUrl}/arquivos`, {withCredentials: true}); 
    }

    getArquivo(id: number): Observable<Arquivo>{
        return this.http.get<Arquivo>(`${this.apiUrl}/arquivos/${id}`, {withCredentials: true});
    } 

    uploadArquivo(conversaId: number, file: File): Observable<string>{
        const formData = new FormData();
        formData.append('file', file);
        return this.http.post(`${this.apiUrl}/conversas/${conversaId}/arquivos`, formData, {withCredentials: true, responseType:'text'});
    }

    deleteArquivo(id: number): Observable<void>{
        return this.http.delete<void>(`${this.apiUrl}/arquivos/${id}`)
    }

    // Método de download que utiliza objeto Blob para baixar arquivos (dados binários)
    downloadArquivo(id: number): Observable<HttpResponse<Blob>> {
        return this.http.get(`${this.apiUrl}/arquivos/${id}/download`, {
            responseType: "blob",
            observe: 'response',
            withCredentials: true
        });
    }
}