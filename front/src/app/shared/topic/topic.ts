import { AsyncPipe, LowerCasePipe, NgClass, UpperCasePipe } from '@angular/common';
import { Component, Input } from '@angular/core';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { GENERIC_LABELS, ME_LABELS, TOPIC_LABELS } from '../labels';
import { MyLoggingService } from '../../core/services/logging.services';
import { TopicService } from '../../core/services/topic.service';
import { UserInfoService } from '../../core/services/user-info.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Topic } from '../../core/models/topic.interface';
import { HttpErrorResponse } from '@angular/common/http';
import { SessionInfo } from '../../core/models/session-info.interface';
import { SessionService } from '../../core/services/session.service';
import { ServerResponse } from '../../core/models/server-response.interface';
import { BackEndTopicArray } from '../../core/models/back-end-topic-array.interface';

@Component({
  selector: 'app-topic-module',
  imports: [UpperCasePipe, LowerCasePipe, AsyncPipe, NgClass],
  templateUrl: './topic.html',
  styleUrl: './topic.scss',
})
export class TopicModule {

  private topicSubject = new BehaviorSubject<Topic[]>([]);
  public topic$ = this.topicSubject.asObservable();

  public idUser!: number ;

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
    private matSnackBar: MatSnackBar,
    private sessionService: SessionService
  ) {  }

  public ngOnInit(): void { 
    
    this.myLog.info("topic.ngOnInit");
    this.idUser = this.sessionService.sessionInformation ? this.sessionService.sessionInformation.id : 0;
    this.myLog.info("idUser=" + this.idUser + " username=" + this.sessionService.sessionInformation?.username);

    if (this.idUser>0){
      this.topicService.all().subscribe({
        next: (response: ServerResponse) => {
              this.myLog.info(this.logPrefix + "  searching all topics in response" );

              try{

                let beTopic = response.data as BackEndTopicArray;
                this.topicSubject.next(beTopic.topics);
                this.onError = false;

              } catch(e: any) {
                this.myLog.error(this.logPrefix + "  all topics in error");
              }
              this.myLog.info(this.logPrefix + "  all topics found" );
            },
        error: (error: HttpErrorResponse) => {
            this.myLog.info(this.logPrefix + " get all topics error : " + error.status.toString() + " " + error.statusText);
            this.onError = true;
            this.matSnackBar.open(
              this.labelsGeneric.error + error.status.toString() + " " + error.statusText, 'Close', { duration: 3000 }
            );
          },
            
        });
      } else {
        this.matSnackBar.open(this.labelsGeneric.msgCnxKo, 'Close', { duration: 3000 });
      }

    }

  public subscription(idTopic: number,event: Event, read: Boolean): void {

    this.myLog.info("Topic subscription : idUser=" + this.idUser + "," + idTopic);
    if (!read){
      //subscribe the topic...
      this.userInfoService.postsubscription(idTopic).subscribe({
      next: (response: ServerResponse) => {//success
          
          try{
              //change the read value of the topiId in the observable topicSubject :
              this.topicSubject.next(
                this.topicSubject.getValue().map(topic =>
                  topic.id === idTopic ? { ...topic, read: true } : topic
                )
              );
              this.matSnackBar.open(this.labelsTopic.topicMsgSouscriptionOk, 'Close', { duration: 3000 });
            } catch (e: any) {
              this.myLog.error(this.logPrefix + "  subscription in error...");
            }
        
        },
      error:(error: HttpErrorResponse) => {
        this.myLog.error( this.logPrefix +  `Error when subscription : ${error.status} - ${error.message}` );
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
      this.userInfoService.postunsubscription(idTopic).subscribe({
      next: (response: ServerResponse) => {//success

          try{ 
              //change the read value of the topiId in the observable topicSubject :
              this.topicSubject.next(
                this.topicSubject.getValue().map(topic =>
                  topic.id === idTopic ? { ...topic, read: false } : topic
                )
              );
              this.matSnackBar.open( this.labelsMe.meUnsubcriptionSuccess , 'Close', { duration: 3000 });
            } catch (e: any) {
              this.myLog.error(this.logPrefix + "  subscription in error...");
            }
          
        },
      error:(error: HttpErrorResponse) => {
          this.myLog.error( `Error when unsubscription : ${error.status} - ${error.message}` );
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
