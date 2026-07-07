import { Component, signal } from '@angular/core';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Header } from './shared/header/header';
import { MyLoggingService } from './core/services/logging.services';
import { registerLocaleData } from '@angular/common';

import { Observable } from 'rxjs/internal/Observable';
import { SessionService } from './core/services/session.service';

import  localeFr  from '@angular/common/locales/fr';

registerLocaleData(localeFr, 'fr');

@Component({
  selector: 'app-root',
  imports: [FormsModule, ReactiveFormsModule, RouterOutlet, Header],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})

export class App {
  
  protected readonly title = signal('front');
  public showHeader = true;

  constructor(private router: Router, private myLog: MyLoggingService, private sessionService: SessionService ){
    router.events.subscribe((val)=> {
      
      if (val instanceof NavigationEnd){
        
        if ((val.url == '/landing') || (val.url == '/')){
          myLog.debug("APP " + val.url + " must not show header");
          this.showHeader = false;
        } else {
          myLog.debug("APP " + val.url + " must show header");
          this.showHeader = true;
        }
        
      }
    })

  }

  public $isLogged(): Observable<boolean> {
    return this.sessionService.$isLogged();
  }

  public logout(): void {
    this.sessionService.logOut();
    this.router.navigate([''])
  }
 
}

