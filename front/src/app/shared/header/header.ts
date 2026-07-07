import { Component } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { MyLoggingService } from '../../core/services/logging.services';
import { MatIcon } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { HEADER_LABELS } from '../labels';
import { SessionService } from '../../core/services/session.service';
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
  public colored_header_article_link = "inactive_link";
  public colored_header_theme_link = "inactive_link";
  public colored_header_disconnect_link = "disconnect";
  public isCollapsed!: boolean;


  constructor(private router: Router, private myLog: MyLoggingService, private sessionService: SessionService ){
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
            this.colored_header_article_link = "inactive_link";
            this.colored_header_theme_link = "inactive_link";
          break;
          case '/theme' :
            myLog.debug("Header " + val.url + " must show right part");
            this.showRight = true;
            this.colored_accont_icon = "account_circle";
            this.colored_header_article_link = "inactive_link";
            this.colored_header_theme_link = "active_link";
          break;
          case '/me' :
            myLog.debug("Header " + val.url + " must show right part");
            this.showRight = true;
            this.colored_accont_icon = "account_circle_me";
            this.colored_header_article_link = "inactive_link";
            this.colored_header_theme_link = "inactive_link";
          break;
          case '/article' :
            myLog.debug("Header " + val.url + " must show right part");
            this.showRight = true;
            this.colored_accont_icon = "account_circle";
            this.colored_header_article_link = "active_link";
            this.colored_header_theme_link = "inactive_link";
          break;
          case '/add-article' :
            myLog.debug("Header " + val.url + " must show right part");
            this.showRight = true;
            this.colored_accont_icon = "account_circle";
            this.colored_header_article_link = "active_link";
            this.colored_header_theme_link = "inactive_link";
          break;
          case '/show-article' :
            myLog.debug("Header " + val.url + " must show right part");
            this.showRight = true;
            this.colored_accont_icon = "account_circle";
            this.colored_header_article_link = "active_link";
            this.colored_header_theme_link = "inactive_link";
            
          break;
        }
      }
    })
  }

  public toggle(){
    this.isCollapsed = ! this.isCollapsed;
  }

  public disconnect(){
    this.myLog.debug("Header : logout...");
    this.sessionService.logOut();
    this.router.navigate([""]);
  }
  
}
