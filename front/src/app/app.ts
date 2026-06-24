import { Component, signal } from '@angular/core';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Header } from './shared/header/header';
import { MyLoggingService } from './core/services/logging.services';
import { registerLocaleData } from '@angular/common';
import localeFr from '@angular/common/locales/fr';
registerLocaleData(localeFr, 'fr');

@Component({
  selector: 'app-root',
  imports: [FormsModule, ReactiveFormsModule, RouterOutlet, Header],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
//@if(this.showHeader){
export class App {
  protected readonly title = signal('front');
  public showHeader = true;
  constructor(private router: Router, private myLog: MyLoggingService ){
    router.events.subscribe((val)=> {
      
      if (val instanceof NavigationEnd){
        
        if ((val.url == '/landing') || (val.url == '/')){
          myLog.info("APP " + val.url + " must not show header");
          this.showHeader = false;
        } else {
          myLog.info("APP " + val.url + " must show header");
          this.showHeader = true;
        }
        
      }
    })

  }
 
}

