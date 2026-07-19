import { ApplicationConfig, provideBrowserGlobalErrorListeners, provideZoneChangeDetection } from '@angular/core';
import { HttpEvent, HttpEventType, HttpHandlerFn, HttpRequest, provideHttpClient, withInterceptors } from '@angular/common/http'
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { Observable } from 'rxjs/internal/Observable';
import { tap } from 'rxjs/internal/operators/tap';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(withInterceptors([authInterceptor]))
  ]
};

//not used
export function loggingInterceptor(
  req: HttpRequest<unknown>,
  next: HttpHandlerFn,
): Observable<HttpEvent<unknown>> {
  return next(req).pipe(
    tap((event) => {
      if (event.type === HttpEventType.Response) {
        console.log(req.url, 'returned a response with status', event.status);
      } else if (event.type === HttpEventType.Sent) {
        const d: Date = new Date();
        console.log(d.toLocaleDateString() + ' ' + d.toLocaleTimeString() + ' ' + req.url, 'Sent a request.');
      }
    }),
  );
}

//
export function authInterceptor(req: HttpRequest<unknown>, next: HttpHandlerFn) {
  // Inject the current `AuthService` and use it to get an authentication token:
  //const authToken = inject(SessionService).sessionInformation?.token;
  const authToken = localStorage.getItem('token');// get the token from browser
  if (req.url.indexOf("login")>0 || req.url.indexOf("register")>0 ){
    return next(req); //no change
  } else {// Clone the request to add the authentication header.
    const jwtReq = req.clone({
      headers: req.headers.append('Authorization', authToken ? "Bearer " +  authToken : ""),
    });
    return next(jwtReq);
  }
}

