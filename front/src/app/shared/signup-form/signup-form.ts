//SignUpForm.ts
import { Component,  Input,  OnInit} from '@angular/core';
import { ME_LABELS, SUBSCRIPTION_LABELS } from '../labels';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatIcon, MatIconModule } from '@angular/material/icon';
import { MyLoggingService } from '../../core/services/logging.services';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';


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

  //log call
  private static nextId = 1;
  public id = SignupForm.nextId++;


  public constructor(private myLog: MyLoggingService,private formBuilder: FormBuilder,){

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
      break;
      case 'me' : 
        this.myTitle = this.labels_me.meTitle;
        this.myBtnLabel = this.labels_me.meBtnSvg;
        this.myUsernameLabel = this.labels_me.meUsername;
        this.myEmailLabel = this.labels_me.meEmail;
        this.myPwdLabel = this.labels_me.meMotDePasse;
      break;
    }
    this.myLog.info("Signup Form ngOnInit ... myTitle=" + this.myTitle + " myBtnLabel=" + this.myBtnLabel);

    this.initForm();
  }

  private initForm(): void {
    this.mySignUpForm = this.formBuilder.group({
        username: ['',
          [Validators.required]
        ],
        email: ['',
          [Validators.required]
        ],
        pwd: ['',
          [Validators.required]
        ]
      });
  }

  public submit(): void{
    window.alert('coucou');
  }
}


