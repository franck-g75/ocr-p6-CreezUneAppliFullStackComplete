import { Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ME_LABELS } from '../../shared/labels';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { Topic } from '../../core/models/topic.interface';
import { MyLoggingService } from '../../core/services/logging.services';
import { TopicService } from '../../core/services/topic-service';
import { UserInfoService } from '../../core/services/user-info-service';
import { UserStore } from '../../core/services/user-store-service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AsyncPipe, LowerCasePipe, NgClass, UpperCasePipe } from '@angular/common';
import { MatButton } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input'

import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { SignupForm } from "../../shared/signup-form/signup-form";

@Component({
  selector: 'app-me-form',
  imports: [FormsModule, ReactiveFormsModule, UpperCasePipe, LowerCasePipe, AsyncPipe, MatButton, NgClass, MatButton,  MatInputModule,  SignupForm],
  templateUrl: './me-form.html',
  styleUrl: './me-form.scss',
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  standalone: true
})
export class MeForm {

  public labels = ME_LABELS;
  public myOrigin = "me";

  public hide: boolean = true;
  public onError: boolean = false;
  public myLoginForm!: FormGroup;
  private topicSubject = new BehaviorSubject<Topic[]>([]);
  public topic$ = this.topicSubject.asObservable();

  public idUser!: number;

  constructor(
    private myLog: MyLoggingService,
    private topicService: TopicService,
    private userInfoService: UserInfoService,
    private userStore: UserStore,
    private matSnackBar: MatSnackBar
  ) {  }

  public ngOnInit(): void { 
    
    this.idUser = this.userStore.getUserId();

    this.topicService.all(this.userStore.getUsername()).subscribe(
      topics => {
        this.topicSubject.next(topics);
      });

  }

  public unsubscription(idTopic: number,event: Event, read: boolean): void {

    this.myLog.info("TopicForm unsubscription : idUser=" + this.idUser + "," + idTopic);
    if (read){
      //unsubscribe the topic...
      this.userInfoService.postunsubscription(this.idUser, idTopic).subscribe({
      next: (_: any) => {

          //change the read value of the topiId in the observable topicSubject :
          this.topicSubject.next(
            this.topicSubject.getValue().map(topic =>
              topic.id === idTopic ? { ...topic, read: false } : topic
            )
          );
        
          this.matSnackBar.open( this.labels.meUnsubcriptionSuccess , 'Close', { duration: 3000 });
        },
      error:(err) => {
        this.myLog.error(
          `Error when unsubscription : ${err.status} - ${err.message}`
        );
        this.matSnackBar.open(
          this.labels.meUnsubcriptionError, 'Close', { duration: 5000 }
        );
      }
    });
    } else {
      // already subscribed 
      alert (this.labels.meNotSubscribed);
    }

  }

}



