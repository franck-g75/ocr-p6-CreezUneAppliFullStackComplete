import { Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatIconButton } from '@angular/material/button';
import { MatFormField } from '@angular/material/form-field';
import { MatCardContent } from '@angular/material/card';
import { MatCardTitle } from '@angular/material/card';
import { MatCard } from '@angular/material/card';
import { MatCardHeader } from '@angular/material/card';
import { MatLabel } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input'
import { MatIcon } from '@angular/material/icon';
import { ActivatedRoute, Router } from '@angular/router';
import { LOGIN_LABELS } from '../../shared/labels';
import { MatSnackBar } from '@angular/material/snack-bar';


@Component({
  selector: 'app-login-form',
  imports: [FormsModule, ReactiveFormsModule, MatIcon, MatIconButton, MatFormField, MatLabel, MatCardContent, MatCardTitle, MatCard, MatCardHeader, MatInputModule],
  templateUrl: './login-form.html',
  styleUrl: './login-form.scss',
})



export class LoginForm implements OnInit{

  labels = LOGIN_LABELS;
  public myLoginForm!: FormGroup;
  

  private initForm(): void {
    this.myLoginForm = this.formBuilder.group({
        string: ['',
          [Validators.required]
        ],
        pwd: ['',
          [Validators.required]
        ]
      });
  }

  public ngOnInit(): void {
    this.initForm();
  }

  constructor(
    private route: ActivatedRoute,
    private matSnackBar: MatSnackBar,
    private formBuilder: FormBuilder,
    private router: Router
  ){  }

  public back() {
    window.history.back();
  }

  public submit(): void { 
    //const loginRequest = this.form.value as LoginRequest;

    window.alert("coucou " + this.myLoginForm.get("string")?.value + ", et ton mot de passe est : " + this.myLoginForm.get("pwd")?.value);

  }

  private exitPage(message: string): void {
    this.matSnackBar.open(message, 'Close', { duration: 3000 });
    this.router.navigate(['topic']);
  }
}
