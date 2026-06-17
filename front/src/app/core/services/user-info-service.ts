import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, } from 'rxjs';
import { MyLoggingService } from './logging.services';
import { UserInfo } from '../models/user-info.interface';

@Injectable({
  providedIn: 'root'
})
export class UserInfoService {

  private pathService = 'api/users';
  private logPrefix: String = "users-services ";

  constructor(
    private myLog: MyLoggingService,
    private httpClient: HttpClient) {  }
 
  public getByString(string: String): Observable<UserInfo> {
    this.myLog.info(this.logPrefix + "getByString("+string+")");
    return this.httpClient.get<UserInfo>(this.pathService + '/' + string );
  }

  public postsubscription(idUser: number,idTopic: number): Observable<void> { 
    this.myLog.info(this.logPrefix + "postsubscription(idUser="+idUser+" idTopic="+idTopic+")");
    return this.httpClient.post<void>(`${this.pathService}/subscribe/idUser/${idUser}/idTopic/${idTopic}`,null);
  }
  
  public postunsubscription(idUser: number,idTopic: number): Observable<void> { 
    this.myLog.info(this.logPrefix + "postunsubscription(idUser="+idUser+" idTopic="+idTopic+")");
    return this.httpClient.post<void>(`${this.pathService}/unsubscribe/idUser/${idUser}/idTopic/${idTopic}`,null);
  }

  public create(userInfo: UserInfo): Observable<UserInfo> {
    this.myLog.info(this.logPrefix + "create("+userInfo.username+")");
    return this.httpClient.post<UserInfo>(this.pathService, userInfo);
  }
  
  public update(userInfo: UserInfo): Observable<UserInfo> {
    this.myLog.info(this.logPrefix + "update("+userInfo.username+")");
    return this.httpClient.put<UserInfo>(this.pathService, userInfo);
  }

}
