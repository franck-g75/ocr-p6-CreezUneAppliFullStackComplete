import { Component,  Input,  OnInit} from '@angular/core';
import { ME_LABELS, SUBSCRIPTION_LABELS } from '../labels';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MyLoggingService } from '../../core/services/logging.services';
import { AbstractControl, FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, ValidatorFn, Validators } from '@angular/forms';
import { UserStore } from '../../core/services/user-store-service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UserInfoService } from '../../core/services/user-info-service';
import { UserInfo } from '../../core/models/user-info.interface';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

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

  @Input({required:true}) public myOrigin!: 'subscription' | 'me';
  public myTitle!: string;
  public myBtnLabel!: string;
  public myUsernameLabel!: string;
  public myEmailLabel!: string;
  public myPwdLabel!: string;

  public mySignUpForm!: FormGroup;

  public hide: boolean = true;
  public onError: boolean = false;

  //for log call
  private static nextId = 1;
  public id = SignupForm.nextId++;
  
  public onUpdate!: boolean;

  public constructor(
    private myLog: MyLoggingService,
    private userInfoService: UserInfoService,
    private formBuilder: FormBuilder,
    private userStore: UserStore,
    private matSnackBar: MatSnackBar,
    private router: Router){

    this.myLog.info(`Signup Form constructor ...#${this.id}...  myOrigin=" + ${this.myOrigin}`);

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
          [Validators.required, Validators.maxLength(50), Validators.minLength(2)]
        ],
        email: [this.userStore.getEmail(),
          [Validators.required, Validators.email, Validators.maxLength(50)]
        ],
        pwd: ['',
          [Validators.required, Validators.minLength(8), Validators.maxLength(50), this.pwdValidator()]
        ],
        id:[this.userStore.getUserId()]
      });

  }


  public pwdValidator(): ValidatorFn {
    const charList=["abcdefghijklmnopqrstuvwxyzéèêàûüç","ABCDEFGHIJKLMNOPQRSTUVWXYZ","0123456789","&#{}()[]-|_@=+$£%µ*,?;.:/!\\"]
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
      return forbidden ? {'forbiddenPassword': {value: control.value}} : null;
    };
  }


  public submit(): void{
    
    const userInfo = this.mySignUpForm?.value as UserInfo;

    if (this.mySignUpForm.valid){
      //window.alert("voila " + msg);.subscribe((_: Session) => this.exitPage('Session created !'));
      if (!this.onUpdate) {
        this.myLog.info(`saving userInfo ${userInfo.username} in progress...`);
        this.userInfoService.create(userInfo).subscribe({
          next: (user: UserInfo) => {
            this.exitPage('User created !');
          },
          error: (err: HttpErrorResponse) => {
            this.myLog.info('User not created !  ' + err.status + ' ' + err.error );
            this.displayError('User not created !  ' + err.error );
          }
        });
      } else {
        this.myLog.info(`updating userInfo ${userInfo.username} in progress...`);
        this.userInfoService.update(userInfo).subscribe({
          next: (user: UserInfo) => {
            this.exitPage('User updated !');
          },
          error: (err: HttpErrorResponse) => {
            this.myLog.info('User not updated !  : ' + err.status + ' ' + err.error );
            this.displayError('User not updated !' + err.error);
          }
        });
      }

    } else {
      
      this.matSnackBar.open("Error(s) found on form validation : fill in every fields. email must be well formatted and password must have at least 8 characters and contain 1 lowercase 1 uppercase 1 number and a special character... ", 'Close', { duration: 10000 });

    }
    
  }

  private exitPage(message: string): void {
    this.matSnackBar.open(message, 'Close', { duration: 3000 });
    this.router.navigate(['/landing']);
  }
  private displayError(message: string): void {
    this.matSnackBar.open(message, 'Close', { duration: 10000 });
  }
}


