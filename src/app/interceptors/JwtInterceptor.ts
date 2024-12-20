import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const token = localStorage.getItem("jwtToken");
        if(token){
            const clonedReq = req.clone({
                setHeaders: {
                    Authorization: `Bearer ${token}`,
                }
            });
            return next.handle(clonedReq);
        }
        return next.handle(req);
    }
}