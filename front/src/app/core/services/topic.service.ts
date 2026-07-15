import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Topic } from '../models/topic.interface';
import { MyLoggingService } from './logging.services';
import { ServerResponse } from '../models/server-response.interface';

@Injectable({
  providedIn: 'root'
})
export class TopicService {

  private pathService = 'api/topic';

  constructor(
    private httpClient: HttpClient,
    private myLog: MyLoggingService,
  ) {
  }

  public all(): Observable<ServerResponse> {
    this.myLog.info("TopicService path = " + this.pathService + "/user" );
    return this.httpClient.get<ServerResponse>(this.pathService + "/user");
  }
  
  public allTopics() : Observable<ServerResponse> {
    this.myLog.info("TopicService path = " + this.pathService);
    return this.httpClient.get<ServerResponse>(this.pathService);
  }

  public getById(idTopic: number): Observable<Topic> {
    this.myLog.info("TopicService path = " + this.pathService + '/' + idTopic);
    return this.httpClient.get<Topic>(this.pathService + '/' + idTopic);
  }
/*
  public all(userid: number): Observable<Topic[]> {
    this.myLog.info("TopicService path = " + this.pathService + "/" + userid);
    return this.httpClient.get<Topic[]>(this.pathService + "/" + userid);
  }*/
}
