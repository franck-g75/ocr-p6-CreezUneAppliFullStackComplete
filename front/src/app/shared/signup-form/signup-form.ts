import { Component,  Input,  OnInit} from '@angular/core';
import { ME_LABELS, SUBSCRIPTION_LABELS, SIGNUP_LABELS, GENERIC_LABELS} from '../labels';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MyLoggingService } from '../../core/services/logging.services';
import { AbstractControl, FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, ValidatorFn, Validators } from '@angular/forms';
import { UserStore } from '../../core/services/user-store.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UserInfo } from '../../core/models/user-info.interface';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { BackEndErrorResponseBody } from '../../core/models/error-response.interface';
import { AuthService } from '../../core/services/auth.service';
import { UserInfoService } from '../../core/services/user-info.service';

@Component({
  selector: 'app-signup',
  imports: [ FormsModule, ReactiveFormsModule, MatCardModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatIconModule, MatInputModule ],
  templateUrl: './signup-form.html',
  styleUrl: './signup-form.scss',
  standalone: true
})

export class SignupForm implements OnInit{
  public labels_me = ME_LABELS;
  public labels_sub = SUBSCRIPTION_LABELS;
  public labels_sign = SIGNUP_LABELS;
  public labels_generic = GENERIC_LABELS;

  @Input({required:true}) public myOrigin!: 'subscription' | 'me';

  public myTitle!: string;
  public myBtnLabel!: string;
  public myUsernameLabel!: string;
  public myEmailLabel!: string;
  public myPwdLabel!: string;

  public mySignUpForm!: FormGroup;
  
  private serverErrorMessage!: String;
  private serverEmailErrorMessage!: string;
  private serverPwdErrorMessage!: string;
  private serverUsernameErrorMessage!: string;

  public hide: boolean = true;
  public onError: boolean = false;

  //for log call
  private static nextId = 1;
  public id = SignupForm.nextId++;
  
  public onUpdate!: boolean;

  public constructor(
    private myLog: MyLoggingService,
    private authService: AuthService,
    private userInfoService: UserInfoService,
    private formBuilder: FormBuilder,
    private userStore: UserStore,
    private matSnackBar: MatSnackBar,
    private router: Router){

  }

  public ngOnInit(){
    
    switch(this.myOrigin){
      case 'subscription' : 
        this.myTitle = this.labels_sub.subscriptionTitle;
        this.myBtnLabel = this.labels_sub.subscriptionBtn;
        this.myUsernameLabel = this.labels_sub.suscriptionNomUtilisateur;
        this.myEmailLabel = this.labels_sub.subscriptionAdresseEmail;
        this.myPwdLabel = this.labels_sub.subinscriptionMotDePasse;
        this.onUpdate = false;
      break;
      case 'me' : 
        this.myTitle = this.labels_me.meTitle;
        this.myBtnLabel = this.labels_me.meBtnSvg;
        this.myUsernameLabel = this.labels_me.meUsername;
        this.myEmailLabel = this.labels_me.meEmail;
        this.myPwdLabel = this.labels_me.meMotDePasse;
        this.onUpdate = true;
      break;
    }
    this.myLog.info("Signup Form ngOnInit ... myTitle=" + this.myTitle + " myBtnLabel=" + this.myBtnLabel);

    this.initForm();
    
  }

  private initForm(): void {

    this.mySignUpForm = this.formBuilder.group({
        username: [this.userStore.getUsername(),
          [Validators.required, Validators.maxLength(20), Validators.minLength(2)]
        ],
        email: [this.userStore.getEmail(),
          [Validators.required, Validators.email, Validators.minLength(6), Validators.maxLength(50)]
        ],
        pwd: ['',
          [Validators.required, Validators.minLength(8), Validators.maxLength(50), this.pwdValidator()]
        ],
        id:[this.userStore.getUserId()]
      });

  }


  public pwdValidator(): ValidatorFn {
    const charList=["abcdefghijklmnopqrstuvwxyzéèêàûüç","ABCDEFGHIJKLMNOPQRSTUVWXYZ","0123456789","&#{}()<>[]-|_@=+$£%µ*,?;.:/!\\"]
    
    return (control: AbstractControl<string>): {[key: string]: any} | null => {
      //is.myLog.info(charList[0]);
      let forbidden: boolean = true;
      let foundMaj: boolean = false;
      let foundMin: boolean = false;
      let foundNb: boolean = false;
      let foundSpecial: boolean = false;
      let i: number = 0;
      let string = control.value;
      if (string.length>0) {
        while(i<string.length && forbidden){
          if (charList[0].indexOf(string.charAt(i))>=0) {foundMin=true;}
          if (charList[1].indexOf(string.charAt(i))>=0) {foundMaj=true;}
          if (charList[2].indexOf(string.charAt(i))>=0) {foundNb=true;}
          if (charList[3].indexOf(string.charAt(i))>=0) {foundSpecial=true;}
          if(foundMin && foundMaj && foundNb && foundSpecial){
            forbidden= false;
          }
          //this.myLog.info(`SignUp-Form pwd validator ${i} : ${string.charAt(i)}  :   min=${foundMin}  maj=${foundMaj}  nb=${foundNb}  special=${foundSpecial} ==> ${forbidden}`);
          i++;
        }
      }
      return forbidden ? {'forbidden password': {value: control.value}} : null;
    };
    
  }

  public submit(): void{
    
    const userInfo = this.mySignUpForm?.value as UserInfo;

    if (this.mySignUpForm.valid){
      if (!this.onUpdate) {
        this.myLog.info(`saving userInfo ${userInfo.username} in progress...`);
        this.authService.create(userInfo).subscribe({
          next: (user: UserInfo) => {
            this.exitPage(this.labels_sub.subscriptionUserCreated);
          },
          error: (error: HttpErrorResponse) => {
            this.myLog.info('srv error found ... ' + error); 
            this.serverError(error);
          }
        });
      } else {
        this.myLog.info(`updating userInfo ${userInfo.username} in progress...`);
        this.userInfoService.update(userInfo).subscribe({
          next: (user: UserInfo) => {
            this.exitPage(this.labels_me.meUserUpdated);
          },
          error: (error: HttpErrorResponse) => {
            this.myLog.info('User not updated !  : ' + error.status + ' ' + error.error );
            this.serverError(error);
          }
        });
      }

    } else {
      
      this.matSnackBar.open(this.labels_generic.clientError, 'Close', { duration: 10000 });

    }
    
  }

  private exitPage(message: string): void {
    this.matSnackBar.open(message, 'Close', { duration: 3000 });
    this.router.navigate(['/landing']);
  }

  public showClientEmailValidationError(): string {
    const emailControl = this.mySignUpForm.get('email');
    if (emailControl?.touched && !emailControl?.valid) {
      if (emailControl?.errors?emailControl.errors['required']:false){
        return this.labels_sign.MandatoryEmail;
      } else if (emailControl?.errors ? emailControl.errors['minlength'] : false){
        return this.labels_sign.EmailMinLength;
      } else if (emailControl?.errors ? emailControl.errors['maxlength'] : false){
        return this.labels_sign.EmailMaxLength;
      } else {
        return "";
      }
    } else {
      return "";
    }
  }

  public showClientUsernameValidationError(){
    const usernameControl = this.mySignUpForm.get('username');
    if (usernameControl?.touched && !usernameControl?.valid) {
      if (usernameControl?.errors?usernameControl.errors['required']:false){
        return this.labels_sign.MandatoryUsername;
      } else if (usernameControl?.errors ? usernameControl.errors['minlength'] : false){
        return this.labels_sign.UsernameMinLength;
      } else if (usernameControl?.errors ? usernameControl.errors['maxlength'] : false){
        return this.labels_sign.UsernameMaxLength;
      } else {
        return "";
      }
    } else {
      return "";
    }
  }

  public showClientPwdValidationError(){
    const pwdControl = this.mySignUpForm.get('pwd');
    if (pwdControl?.touched && !pwdControl?.valid) {
      if (pwdControl?.errors?pwdControl.errors['required']:false){
        return this.labels_sign.MandatoryPwd;
      } else if (pwdControl?.errors ? pwdControl.errors['minlength'] : false){
        return this.labels_sign.PwdMinLength;
      } else if (pwdControl?.errors ? pwdControl.errors['maxlength'] : false){
        return this.labels_sign.PwdMaxLength;
      } else if (this.pwdValidator()!==null){
        return this.labels_sign.PwdValidator;
      } else {
        return "";
      }
    } else {
      return "";
    }
  }


  public showServerEmailValidationError(): string {
    return this.serverEmailErrorMessage!==undefined ? this.serverEmailErrorMessage : '';
  }
  public showServerPwdValidationError(): string {
    return this.serverPwdErrorMessage!==undefined ? this.serverPwdErrorMessage : '';
  }
  public showServerUsernameValidationError(): string {
    return this.serverUsernameErrorMessage!==undefined ? this.serverUsernameErrorMessage : '';
  }

  public showServerErrorMessage(){
    return this.serverErrorMessage!==null ? this.serverErrorMessage : '';
  }

  public  serverError(error : HttpErrorResponse): void{
    const backEndResponseBody = error.error as BackEndErrorResponseBody;
    if (backEndResponseBody!==null && backEndResponseBody!==undefined){
      this.serverEmailErrorMessage = "";
      this.serverPwdErrorMessage = "";
      this.serverUsernameErrorMessage = "";
      this.serverErrorMessage = "";
      if (error.status === 0) {
        console.error('Network error:', error.error);
      } else {
        if (backEndResponseBody.validationErrors!==null && backEndResponseBody.validationErrors !==undefined ) {
          this.myLog.error(`Backend returned code ${error.status}, body:`, error.error);
          if (backEndResponseBody.validationErrors.email!==undefined) {
            this.myLog.error(`Backend returned ${backEndResponseBody.validationErrors.email}`);
            this.serverEmailErrorMessage = backEndResponseBody.validationErrors.email;        
          }
          if (backEndResponseBody.validationErrors.pwd!==undefined) {
            this.myLog.error(`Backend returned ${backEndResponseBody.validationErrors.pwd}`);
            this.serverPwdErrorMessage = backEndResponseBody.validationErrors.pwd;        
          }
          if (backEndResponseBody.validationErrors.username!==undefined) {
            this.myLog.error(`Backend returned ${backEndResponseBody.validationErrors.username}`);
            this.serverUsernameErrorMessage = backEndResponseBody.validationErrors.username;
          }
        } else { //validationErrrors==nuul 
          this.myLog.error(`Backend returned ${backEndResponseBody.errorMessage}`);
          this.serverErrorMessage = this.labels_sign.ServerResponds + backEndResponseBody.errorMessage;
        }
      }
    } else {
      this.myLog.error(`Backend returned  ${error.status}  ${error.statusText}  ${error.message}`);
      this.serverErrorMessage = this.labels_sign.ServerResponds + error.status + "  " +  error.statusText + " " + error.message;
    }
  }
}

