import { Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatIconButton } from '@angular/material/button';
import { MatFormField } from '@angular/material/form-field';
import { MatCardContent } from '@angular/material/card';
import { MatCard } from '@angular/material/card';
import { MatLabel } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input'
import { MatIcon } from '@angular/material/icon';
import { GENERIC_LABELS, LOGIN_LABELS } from '../../shared/labels';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from '../../core/services/auth.service';
import { MyLoggingService } from '../../core/services/logging.services';
import { LoginRequest } from '../../core/models/Login-request.interface';
import { SessionService } from '../../core/services/session.service';
import { ServerResponse } from '../../core/models/server-response.interface';
import { SessionInfo } from '../../core/models/session-info.interface';
import { BackEndValidationErrors } from '../../core/models/back-end-validation-errors.interface';
import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';


@Component({
  selector: 'app-login-form',
  imports: [FormsModule, ReactiveFormsModule,  MatIcon, MatIconButton, MatFormField, MatLabel, MatCardContent,  MatCard, MatInputModule],
  templateUrl: './login-form.html',
  styleUrl: './login-form.scss',
})

export class LoginForm implements OnInit{

  labels = LOGIN_LABELS;
  labels_generic = GENERIC_LABELS;

  private serverErrorMessage!: String;

  public hide = true; //hidden password by default
  public myLoginForm!: FormGroup;
  public onError = false;
  private logPrefix: String = "login-Form ";

  private initForm(): void {
    this.myLoginForm = this.formBuilder.group({
        str: ['',
          [Validators.required]
        ],
        pwd: ['',
          [Validators.required]
        ]
      });
  }

  public ngOnInit(): void {
    this.myLog.debug(this.logPrefix + " ngOnInit");
    this.initForm();
  }

  constructor(
    private myLog: MyLoggingService,
    private sessionService: SessionService,
    private router: Router,
    private matSnackBar: MatSnackBar,
    private formBuilder: FormBuilder,
    private authService: AuthService
  ){  }

  public back() {
    window.history.back();
  }

  public submit(): void { 
    this.myLog.info(this.logPrefix + "appel du service");

    const lr: LoginRequest = { str: this.myLoginForm.get("str")?.value, pwd: this.myLoginForm.get("pwd")?.value }

    this.authService.login(lr).subscribe({
      next: (response: ServerResponse) => {
        //serverResponse.message
        //serverResponse.code
        //serverResponse.data (can be null)
        const backEndResponseData = response.data as SessionInfo;
        this.myLog.info(this.logPrefix + " User trouvé : " + backEndResponseData.id + " " + backEndResponseData.email + " " + backEndResponseData.username + " " + backEndResponseData.token );
        
        localStorage.setItem('token', backEndResponseData.token);// store the token in browser
        this.sessionService.logIn(backEndResponseData);
        this.router.navigate(['article']);
      },
      error : (error: HttpErrorResponse) => {
        this.myLog.info(this.logPrefix + "error : " + JSON.stringify(error).toString());
        this.onError = true;
        this.serverError(error);
      }
    })

  }
  
  public showServerErrorMessage(){
    return this.serverErrorMessage!==null ? this.serverErrorMessage : '';
  }

  public serverError(error : HttpErrorResponse): void {
      
      /*
      
      HttpErrorResponse structure : 
      error.message
      error.status
      error.statusText
      error.error.message
      error.error.code
      error.error.data
      error.error & error.error.data can be null

      */

      this.serverErrorMessage = "";
      
      if (error.error!==null){
      
        const backEndResponseData = error.error.data as BackEndValidationErrors;

        if (backEndResponseData!==null && backEndResponseData!==undefined){
          
            this.myLog.error(`Backend returned ${error.error.message}`);
            this.serverErrorMessage = error.error.message;
            
          } else {//error.error.data==null
        
            this.myLog.error(`Backend returned ${error.status}  ${error.message}  ${error.error.code}  ${error.error.message}`);
            this.serverErrorMessage =  error.error.message;
        
          }
      } else {//error.error==null

        this.myLog.error(`Backend returned ${error.status}  ${error.statusText}  ${error.message} `);
        this.serverErrorMessage =  error.status + " " + error.statusText + "  " + ((error.status===500) ? this.labels_generic.TryAgainLater : "") ;
        
      }
    }
}

//
//useless : https://stackoverflow.com/questions/3715920/is-it-worth-hashing-passwords-on-the-client-side
/*
function hashPassword( password: string ): string {
  return bcrypt.hashSync(password, 10);
}
*/
