import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, throwError, } from 'rxjs';
import { MyLoggingService } from './logging.services';
import { UserInfo } from '../models/user-info.interface';
import { BackEndErrorResponseBody } from '../models/error-response.interface';
import { ValidationErrors } from '@angular/forms';
import { BackEndValidationErrors } from '../models/back-end-validation-errors.interface';

@Injectable({
  providedIn: 'root'
})
export class UserInfoService {

  private pathService = 'api/users';
  private logPrefix: String = "users-services ";

  constructor(
    private myUserSvcLog: MyLoggingService,
    private httpClient: HttpClient) {  }
 
  public getByString(string: String): Observable<UserInfo> {
    this.myUserSvcLog.info(this.logPrefix + "getByString("+string+")");
    return this.httpClient.get<UserInfo>(this.pathService + '/' + string );
  }

  public postsubscription(idUser: number,idTopic: number): Observable<void> { 
    this.myUserSvcLog.info(this.logPrefix + "postsubscription(idUser="+idUser+" idTopic="+idTopic+")");
    return this.httpClient.post<void>(`${this.pathService}/subscribe/idUser/${idUser}/idTopic/${idTopic}`,null);
  }
  
  public postunsubscription(idUser: number,idTopic: number): Observable<void> { 
    this.myUserSvcLog.info(this.logPrefix + "postunsubscription(idUser="+idUser+" idTopic="+idTopic+")");
    return this.httpClient.post<void>(`${this.pathService}/unsubscribe/idUser/${idUser}/idTopic/${idTopic}`,null);
  }

  public create(userInfo: UserInfo): Observable<UserInfo> {
    this.myUserSvcLog.info(this.logPrefix + "create("+userInfo.username+")");
    return this.httpClient.post<UserInfo>(this.pathService, userInfo);
  }
  
  public update(userInfo: UserInfo): Observable<UserInfo> {
    this.myUserSvcLog.info(this.logPrefix + "update("+userInfo.username+")");
    return this.httpClient.put<UserInfo>(this.pathService, userInfo);
  }
/*
  //pas d'accès aux donnees membre de la classe (ni this.logPrefix ni this.myUserSvcLog)
  private handleError(error: HttpErrorResponse):Observable<never>{ 
    const validationErrors = error.error as ErrorResponse;
    let msg: String[] = [""];
    if (error.error!==null){
      
      if (error.status === 0) {
        this.myUserSvcLog.error('Network error:', error.error)
        msg.push("erreur réseau");
      } else {
        if (error.error!==null) {
          console.error(`Backend returned code ${error.status}, body:`, error.error);
          if (error.error.validationErrors.email!==null) {
            console.error(`Backend returned ${error.error.validationErrors.email}`);
            msg.push(error.error.validationErrors.email);        
          }
          if (error.error.validationErrors.pwd!==null) {
            console.error(`Backend returned ${error.error.validationErrors.pwd}`);
            msg.push(error.error.validationErrors.pwd);        
          }
          if (error.error.validationErrors.username!==null) {
            console.error(`Backend returned ${error.error.validationErrors.username}`);
            msg.push(error.error.validationErrors.username);        
          }
        }
      }
    }
    return throwError(() => new Error(msg.join("<br/>"),));
  }
*/
}