import { Component } from '@angular/core';
import { TopicService } from '../../core/services/topic-service';
import { Topic } from '../../core/models/topic.interface';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { AsyncPipe, LowerCasePipe, UpperCasePipe } from '@angular/common';
import { MatButton } from '@angular/material/button';
import { UserStore } from '../../core/services/user-store-service';
import { TOPIC_LABELS } from '../../shared/labels';
import { UsersService } from '../../core/services/users-service';
import { MyLoggingService } from '../../core/services/logging.services';
import { MatSnackBar } from '@angular/material/snack-bar';
import { NgClass } from '@angular/common';

@Component({
  selector: 'app-topic',
  imports: [UpperCasePipe, LowerCasePipe, AsyncPipe, MatButton, NgClass],
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
    private usersService: UsersService,
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
      this.usersService.postsubscription(this.idUser, idTopic).subscribe({
      next: (_: any) => {

          //change the read value of the topiId in the observable topicSubject :
          this.topicSubject.next(
            this.topicSubject.getValue().map(topic =>
              topic.id === idTopic ? { ...topic, read: true } : topic
            )
          );
        
          this.matSnackBar.open('subscription registrered !', 'Close', { duration: 3000 });
        },
      error:(err) => {
        this.myLog.error(
        `Erreur lors de la souscription : ${err.status} - ${err.message}`
        );
        this.matSnackBar.open(
          'Erreur lors de la souscription : veuillez contacter l\'administrateur', 'Close', { duration: 5000 }
        );
      }
    });
    } else {
      // already subscribed 
      alert ("déjà abonné ! ");
    }

  }

}


/*


@if(topic.read){
    <button 
        id ="topic.id" 
        matButton="outlined" 
        class="custom-btn-grey">{{labels.topicDejaAbonne}}
    </button>  
}
@else { 
    <button 
        id="topic.id" 
        matButton="filled" 
        class="custom-btn" 
        (click)="subscription(topic.id,$event)">{{labels.topicSabonner}}
    </button> 
}






*/