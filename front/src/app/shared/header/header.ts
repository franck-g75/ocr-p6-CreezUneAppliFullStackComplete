import { Component } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { MyLoggingService } from '../../core/services/logging.services';
import { MatIcon } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { HEADER_LABELS } from '../labels';
@Component({
  selector: 'app-header',
  imports: [MatIcon,  RouterLink],
  templateUrl: './header.html',
  styleUrl: './header.scss',
})

export class Header {
 
  public labels = HEADER_LABELS;
  public showRight = false;
  public colored_accont_icon = "account_circle";

  constructor(private router: Router, private myLog: MyLoggingService ){
    router.events.subscribe((val)=> {
      
      if (val instanceof NavigationEnd){
        
        switch(val.url){
          case '/subscription' :
            myLog.debug("Header " + val.url + " must not show right part");
            this.showRight = false;
            this.colored_accont_icon = "account_circle";
          break;
          case '/login' :
            myLog.debug("Header " + val.url + " must not show right part");
            this.showRight = false;
            this.colored_accont_icon = "account_circle";
          break;
          case '/topic' :
            myLog.debug("Header " + val.url + " must show right part");
            this.showRight = true;
            this.colored_accont_icon = "account_circle";
          break;
          case '/me' :
            myLog.debug("Header " + val.url + " must show right part");
            this.showRight = true;
            this.colored_accont_icon = "account_circle_me";
          break;
        }
      }
    })
  }
}
