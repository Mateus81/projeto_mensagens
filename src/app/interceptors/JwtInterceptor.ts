import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, throwError, catchError, switchMap } from "rxjs"; 
import { AuthService } from "../service/auth.service";

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
    constructor(private authService: AuthService){}

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const token = localStorage.getItem("jwtToken");
        if(token){
            const clonedReq = req.clone({
                setHeaders: {
                    Authorization: `Bearer ${token}`,
                }
            });
            return next.handle(clonedReq).pipe(
                catchError((error: HttpErrorResponse) => {
                    if(error.status === 401){
                        return this.authService.refreshToken().pipe(
                            switchMap((newToken: string) => {
                                localStorage.setItem("jwtToken", newToken);
                                const newReq = req.clone({
                                    setHeaders: { 
                                    Authorization: `Bearer ${newToken}`
                                }});
                                return next.handle(newReq);
                            }),
                            catchError((refreshError) => {
                                this.authService.logout();
                                return throwError(refreshError);
                            })
                        );
                    }
                    return throwError(error);
                })
            );
        }
        return next.handle(req);
    }
}