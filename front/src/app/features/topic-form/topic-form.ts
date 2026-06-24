import { Component } from '@angular/core';
import { TopicService } from '../../core/services/topic-service';
import { Topic } from '../../core/models/topic.interface';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { UserStore } from '../../core/services/user-store-service';
import { TOPIC_LABELS } from '../../shared/labels';
import { UserInfoService } from '../../core/services/user-info-service';
import { MyLoggingService } from '../../core/services/logging.services';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-topic',
  imports: [],
  templateUrl: './topic-form.html',
  styleUrl: './topic-form.scss',
})
export class TopicForm {
  
  private topicSubject = new BehaviorSubject<Topic[]>([]);
  public topic$ = this.topicSubject.asObservable();

  public idUser!: number;

  public labels = TOPIC_LABELS;

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

    this.myLog.info("TopicForm subscription : idUser=" + this.idUser + "," + idTopic);
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
        
          this.matSnackBar.open(this.labels.topicMsgSouscriptionOk, 'Close', { duration: 3000 });
        },
      error:(err) => {
        this.myLog.error(
        `Erreur lors de la souscription : ${err.status} - ${err.message}`
        );
        this.matSnackBar.open(
          this.labels.topicMsgSouscriptionKo, 'Close', { duration: 5000 }
        );
      }
    });
    } else {
      // already subscribed 
      alert (this.labels.topicDejaAbonne);
    }

  }

}

