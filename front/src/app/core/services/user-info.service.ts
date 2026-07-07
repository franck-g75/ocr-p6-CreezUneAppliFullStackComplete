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
 
  

  public postsubscription(idUser: number,idTopic: number): Observable<void> { 
    this.myUserSvcLog.info(this.logPrefix + "postsubscription(idUser="+idUser+" idTopic="+idTopic+")");
    return this.httpClient.post<void>(`${this.pathService}/subscribe/idUser/${idUser}/idTopic/${idTopic}`,null);
  }
  
  public postunsubscription(idUser: number,idTopic: number): Observable<void> { 
    this.myUserSvcLog.info(this.logPrefix + "postunsubscription(idUser="+idUser+" idTopic="+idTopic+")");
    return this.httpClient.post<void>(`${this.pathService}/unsubscribe/idUser/${idUser}/idTopic/${idTopic}`,null);
  }

  public update(userInfo: UserInfo): Observable<ServerResponse> {
    this.myUserSvcLog.info(this.logPrefix + "update("+userInfo.username+")");
    return this.httpClient.put<ServerResponse>(this.pathService, userInfo);
  }

}