import { Component, CUSTOM_ELEMENTS_SCHEMA, OnInit } from '@angular/core';
import { GENERIC_LABELS, ME_LABELS } from '../../shared/labels';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { Topic } from '../../core/models/topic.interface';
import { MatInputModule } from '@angular/material/input'
import { FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SignupForm } from "../../shared/signup-form/signup-form";
import { TopicModule } from "../../shared/topic/topic";
import { SessionService } from '../../core/services/session.service';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';


@Component({
  selector: 'app-me-form',
  imports: [FormsModule, ReactiveFormsModule, MatInputModule, SignupForm, TopicModule],
  templateUrl: './me-form.html',
  styleUrl: './me-form.scss',
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  standalone: true
})
export class MeForm implements OnInit{

  public idUser!: number;
  public labelsGeneric = GENERIC_LABELS;

  constructor(
      private matSnackBar: MatSnackBar,
      private router: Router,
      private sessionService: SessionService) {  }

  public ngOnInit(){
    this.idUser = this.sessionService.sessionInformation ? this.sessionService.sessionInformation.id : 0;
    if (this.idUser<=0){
      this.matSnackBar.open(this.labelsGeneric.msgCnxKo, 'Close', { duration: 3000 });
      this.router.navigate(['landing']);
    }
  }
}



