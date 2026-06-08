import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Topic } from '../models/topic.interface';
import { MyLoggingService } from './logging.services';

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

  public all(username: string): Observable<Topic[]> {
    this.myLog.info("TopicService path = " + this.pathService + "/" + username);
    return this.httpClient.get<Topic[]>(this.pathService + "/" + username);
  }
/*
  public all(userid: number): Observable<Topic[]> {
    this.myLog.info("TopicService path = " + this.pathService + "/" + userid);
    return this.httpClient.get<Topic[]>(this.pathService + "/" + userid);
  }*/
}
