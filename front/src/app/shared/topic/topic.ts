import { AsyncPipe, LowerCasePipe, NgClass, UpperCasePipe } from '@angular/common';
import { Component, Input } from '@angular/core';
import { MatButton } from '@angular/material/button';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { ME_LABELS, TOPIC_LABELS } from '../labels';
import { MyLoggingService } from '../../core/services/logging.services';
import { TopicService } from '../../core/services/topic-service';
import { UserInfoService } from '../../core/services/user-info-service';
import { UserStore } from '../../core/services/user-store-service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Topic } from '../../core/models/topic.interface';

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

  @Input({required:true}) public myOrigin!: 'theme' | 'me';
  
  public labelsMe = ME_LABELS;
  public labelsTopic = TOPIC_LABELS;

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

  public subscription(idTopic: number,event: Event, read: Boolean): void {

    
    this.myLog.info("Topic subscription : idUser=" + this.idUser + "," + idTopic);
    if (!read){
      //subscribe the topic...
      this.userInfoService.postsubscription(this.idUser, idTopic).subscribe({
      next: (_: any) => {

          //change the read value of the topiId in the observable topicSubject :
          this.topicSubject.next(
            this.topicSubject.getValue().map(topic =>
              topic.id === idTopic ? { ...topic, read: true } : topic
            )
          );
        
          this.matSnackBar.open(this.labelsTopic.topicMsgSouscriptionOk, 'Close', { duration: 3000 });
        },
      error:(err) => {
        this.myLog.error( `Erreur lors de la souscription : ${err.status} - ${err.message}` );
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
      // already subscribed 
      alert (this.labelsMe.meNotSubscribed);
    }

  }

}
