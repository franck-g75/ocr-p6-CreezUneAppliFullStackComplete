import { AsyncPipe, LowerCasePipe, NgClass, UpperCasePipe } from '@angular/common';
import { Component, Input } from '@angular/core';
import { MatButton } from '@angular/material/button';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { GENERIC_LABELS, ME_LABELS, TOPIC_LABELS } from '../labels';
import { MyLoggingService } from '../../core/services/logging.services';
import { TopicService } from '../../core/services/topic-service';
import { UserInfoService } from '../../core/services/user-info-service';
import { UserStore } from '../../core/services/user-store-service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Topic } from '../../core/models/topic.interface';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-topic-module',
  imports: [UpperCasePipe, LowerCasePipe, AsyncPipe, MatButton, NgClass],
  templateUrl: './topic.html',
  styleUrl: './topic.scss',
})
export class TopicModule {

  private topicSubject = new BehaviorSubject<Topic[]>([]);
  public topic$ = this.topicSubject.asObservable();

  public idUser!: number;

  private logPrefix: string = "TopicModule";
  private onError: boolean = false;

  @Input({required:true}) public myOrigin!: 'theme' | 'me';
  
  public labelsMe = ME_LABELS;
  public labelsTopic = TOPIC_LABELS;
  public labelsGeneric = GENERIC_LABELS;
   
  constructor(
    private myLog: MyLoggingService,
    private topicService: TopicService,
    private userInfoService: UserInfoService,
    private userStore: UserStore,
    private matSnackBar: MatSnackBar
  ) {  }

  public ngOnInit(): void { 
    
    this.idUser = this.userStore.getUserId();

    this.topicService.all(this.userStore.getUsername()).subscribe({
      next: (topics: Topic[]) => {
            this.topicSubject.next(topics);
            this.myLog.info(this.logPrefix + "all topics found" );
            this.onError = false;
          },
      error: (error: HttpErrorResponse) => {
          this.myLog.info(this.logPrefix + " get all topics error : " + error.status.toString() + " " + error.statusText);
          this.onError = true;
          this.matSnackBar.open(
            this.labelsGeneric.error + error.status.toString() + " " + error.statusText, 'Close', { duration: 3000 }
          );
        },
          
      });
    }

  public subscription(idTopic: number,event: Event, read: Boolean): void {

    this.myLog.info("Topic subscription : idUser=" + this.idUser + "," + idTopic);
    if (!read){
      //subscribe the topic...
      this.userInfoService.postsubscription(this.idUser, idTopic).subscribe({
      next: (_: any) => {
          //success
          //change the read value of the topiId in the observable topicSubject :
          this.topicSubject.next(
            this.topicSubject.getValue().map(topic =>
              topic.id === idTopic ? { ...topic, read: true } : topic
            )
          );
        
          this.matSnackBar.open(this.labelsTopic.topicMsgSouscriptionOk, 'Close', { duration: 3000 });
        },
      error:(err) => {
        this.myLog.error( `Error when subscription : ${err.status} - ${err.message}` );
        this.matSnackBar.open(
          this.labelsTopic.topicMsgSouscriptionKo, 'Close', { duration: 5000 }
        );
      }
    });
    } else {
      // already subscribed 
      alert (this.labelsTopic.topicDejaAbonne);
    }

  }


  public unsubscription(idTopic: number,event: Event, read: boolean): void {
    
    this.myLog.info("Topic unsubscription : idUser=" + this.idUser + "," + idTopic);

    if (read){
      //unsubscribe the topic...
      this.userInfoService.postunsubscription(this.idUser, idTopic).subscribe({
      next: (_: any) => {
          //success
          //change the read value of the topiId in the observable topicSubject :
          this.topicSubject.next(
            this.topicSubject.getValue().map(topic =>
              topic.id === idTopic ? { ...topic, read: false } : topic
            )
          );
        
          this.matSnackBar.open( this.labelsMe.meUnsubcriptionSuccess , 'Close', { duration: 3000 });
        },
      error:(err) => {
        this.myLog.error( `Error when unsubscription : ${err.status} - ${err.message}` );
        this.matSnackBar.open(
          this.labelsMe.meUnsubcriptionError, 'Close', { duration: 5000 }
        );
      }
    });
    } else {
      // already unsubscribed 
      alert (this.labelsMe.meNotSubscribed);
    }

  }

}
