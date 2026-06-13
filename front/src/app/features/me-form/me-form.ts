import { Component } from '@angular/core';
import { ME_LABELS } from '../../shared/labels';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { Topic } from '../../core/models/topic.interface';
import { MyLoggingService } from '../../core/services/logging.services';
import { TopicService } from '../../core/services/topic-service';
import { UsersService } from '../../core/services/users-service';
import { UserStore } from '../../core/services/user-store-service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AsyncPipe, LowerCasePipe, NgClass, UpperCasePipe } from '@angular/common';
import { MatButton, MatIconButton } from '@angular/material/button';
import { MatFormField } from '@angular/material/form-field';
import { MatCardContent } from '@angular/material/card';
import { MatCardTitle } from '@angular/material/card';
import { MatCard } from '@angular/material/card';
import { MatCardHeader } from '@angular/material/card';
import { MatLabel } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input'
import { MatIcon } from '@angular/material/icon';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-me-form',
  imports: [FormsModule, ReactiveFormsModule, UpperCasePipe, LowerCasePipe, AsyncPipe, MatButton, NgClass, MatButton, MatCard, MatCardContent,  MatLabel, MatIcon, MatIconButton, MatInputModule, MatFormField],
  templateUrl: './me-form.html',
  styleUrl: './me-form.scss',
})
export class MeForm {

  public labels = ME_LABELS;

  public hide: boolean = true;
  public onError: boolean = false;
  public myLoginForm!: FormGroup;
  private topicSubject = new BehaviorSubject<Topic[]>([]);
  public topic$ = this.topicSubject.asObservable();

  public idUser!: number;

  constructor(
    private myLog: MyLoggingService,
    private topicService: TopicService,
    private usersService: UsersService,
    private formBuilder: FormBuilder,
    private userStore: UserStore,
    private matSnackBar: MatSnackBar
  ) {  }

  public ngOnInit(): void { 
    
    this.idUser = this.userStore.getUserId();

    this.topicService.all(this.userStore.getUsername()).subscribe(
      topics => {
        this.topicSubject.next(topics);
      });

      this.initForm();

    }


  private initForm(): void {
    this.myLoginForm = this.formBuilder.group({
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




  public unsubscription(idTopic: number,event: Event, read: boolean): void {

    this.myLog.info("TopicForm unsubscription : idUser=" + this.idUser + "," + idTopic);
    if (read){
      //unsubscribe the topic...
      this.usersService.postunsubscription(this.idUser, idTopic).subscribe({
      next: (_: any) => {

          //change the read value of the topiId in the observable topicSubject :
          this.topicSubject.next(
            this.topicSubject.getValue().map(topic =>
              topic.id === idTopic ? { ...topic, read: false } : topic
            )
          );
        
          this.matSnackBar.open('unsubscription registrered !', 'Close', { duration: 3000 });
        },
      error:(err) => {
        this.myLog.error(
        `Error when unsubscription : ${err.status} - ${err.message}`
        );
        this.matSnackBar.open(
          'Error when unsubscription : contact the administrator', 'Close', { duration: 5000 }
        );
      }
    });
    } else {
      // already subscribed 
      alert ("not subscribed ! ");
    }

  }

  public submit(): void {
    window.alert("submit clicked !!");
  }

}
