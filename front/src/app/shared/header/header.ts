import { Component } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { MyLoggingService } from '../../core/services/logging.services';
import { MatIcon } from '@angular/material/icon';

@Component({
  selector: 'app-header',
  imports: [MatIcon],
  templateUrl: './header.html',
  styleUrl: './header.scss',
})
export class Header {
 
  public showRight= false;

  constructor(private router: Router, private myLog: MyLoggingService ){
    router.events.subscribe((val)=> {
      
      if (val instanceof NavigationEnd){
        

        switch(val.url){
          case '/subscription' :
            myLog.info("Header " + val.url + " must not show right part");
            this.showRight = false;
          break;
          case '/login' :
            myLog.info("Header " + val.url + " must not show right part");
            this.showRight = false;
          break;
          case '/topic' :
            myLog.info("Header " + val.url + " must show right part");
            this.showRight = true;
          break;
        }

      }

    })
  }

}
