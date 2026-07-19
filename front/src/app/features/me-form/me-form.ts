import { Component, CUSTOM_ELEMENTS_SCHEMA, OnInit } from '@angular/core';
import { GENERIC_LABELS } from '../../shared/labels';
import { MatInputModule } from '@angular/material/input'
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
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
      private router: Router) {  }

  public ngOnInit(){
    
    if (localStorage.getItem('token')==null || localStorage.getItem('token')==undefined){
      this.matSnackBar.open(this.labelsGeneric.msgCnxKo, 'Close', { duration: 3000 });
      this.router.navigate(['landing']);
    } 

  }
}



