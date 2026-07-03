import { Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import {  MatIconButton } from '@angular/material/button';
import { MatFormField } from '@angular/material/form-field';
import { MatCardContent } from '@angular/material/card';
import { MatCard } from '@angular/material/card';
import { MatLabel } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input'
import { MatIcon } from '@angular/material/icon';
import { ActivatedRoute, Router } from '@angular/router';
import { LOGIN_LABELS } from '../../shared/labels';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from '../../core/services/auth.service';
import { UserInfo } from '../../core/models/user-info.interface';
import { MyLoggingService } from '../../core/services/logging.services';
import { UserStore } from '../../core/services/user-store.service';

@Component({
  selector: 'app-login-form',
  imports: [FormsModule, ReactiveFormsModule,  MatIcon, MatIconButton, MatFormField, MatLabel, MatCardContent,  MatCard, MatInputModule],
  templateUrl: './login-form.html',
  styleUrl: './login-form.scss',
})

export class LoginForm implements OnInit{

  labels = LOGIN_LABELS;
  public hide = true; //hidden password by default
  public myLoginForm!: FormGroup;
  public onError = false;
  private logPrefix: String = "login-Form ";

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
    private myLog: MyLoggingService,
    private userStore: UserStore,
    private route: ActivatedRoute,
    private matSnackBar: MatSnackBar,
    private formBuilder: FormBuilder,
    private router: Router,
    private authService: AuthService
  ){  }

  public back() {
    window.history.back();
  }

  public submit(): void { 
    //const loginRequest = this.form.value as LoginRequest;

    //window.alert("coucou " + this.myLoginForm.get("string")?.value + ", et ton mot de passe est : " + this.myLoginForm.get("pwd")?.value);
    this.myLog.info(this.logPrefix + "appel du service");
    this.authService.getByString(this.myLoginForm.get("string")?.value).subscribe({
      next: (response: UserInfo) => {
        this.myLog.info(this.logPrefix + " User trouvé : " + response.id + " " + response.email + " " + response.username );
        this.onError = false;
        this.userStore.setEmail(response.email);
        this.userStore.setUsername(response.username);
        this.userStore.setUserId(response.id);
        this.router.navigate(['article']);
      },
      error : (error: Error) => {
        //this.myLog.info(this.logPrefix + "error : " + JSON.stringify(error).toString());
        this.onError = true;
      }
    })

  }

}
