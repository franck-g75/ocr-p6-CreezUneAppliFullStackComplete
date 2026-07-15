import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MyLoggingService } from './logging.services';
import { UserInfo } from '../models/user-info.interface';
import { Observable } from 'rxjs/internal/Observable';
import { ServerResponse } from '../models/server-response.interface';

@Injectable({
  providedIn: 'root'
})
export class UserInfoService {

  private pathService = 'api/users';
  private logPrefix: String = "users-services ";

  constructor(
    private myUserSvcLog: MyLoggingService,
    private httpClient: HttpClient) {  }
 
  

  public postsubscription(idTopic: number): Observable<ServerResponse> { 
    this.myUserSvcLog.info(this.logPrefix + "postsubscription(topic="+idTopic+")");
    return this.httpClient.post<ServerResponse>(`${this.pathService}/subscribe/topic/${idTopic}`,null);
  }
  
  public postunsubscription(idTopic: number): Observable<ServerResponse> { 
    this.myUserSvcLog.info(this.logPrefix + "postunsubscription(topic="+idTopic+")");
    return this.httpClient.post<ServerResponse>(`${this.pathService}/unsubscribe/topic/${idTopic}`,null);
  }

  public update(userInfo: UserInfo): Observable<ServerResponse> {
    this.myUserSvcLog.info(this.logPrefix + "update("+userInfo.username+")");
    return this.httpClient.put<ServerResponse>(this.pathService, userInfo);
  }

}