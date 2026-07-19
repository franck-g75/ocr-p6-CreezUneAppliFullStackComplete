import { Component, OnInit } from '@angular/core';
import { TopicModule } from '../../shared/topic/topic';
import { GENERIC_LABELS } from '../../shared/labels';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { SessionService } from '../../core/services/session.service';

@Component({
  selector: 'app-theme',
  imports: [TopicModule],
  templateUrl: './theme.html',
  styleUrl: './theme.scss',
})
export class Theme implements OnInit {
  public idUser!: number;
  public labelsGeneric = GENERIC_LABELS;

  constructor(
      private matSnackBar: MatSnackBar,
      private router: Router
    ) {  }

  public ngOnInit(){

    if (localStorage.getItem('token')==null || localStorage.getItem('token')==undefined){
      this.matSnackBar.open(this.labelsGeneric.msgCnxKo, 'Close', { duration: 3000 });
      this.router.navigate(['landing']);
    } 

  }
}
