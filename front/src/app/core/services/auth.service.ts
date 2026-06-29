import { Injectable } from "@angular/core";
import { MyLoggingService } from "./logging.services";
import { HttpClient } from "@angular/common/http";
import { UserInfo } from "../models/user-info.interface";
import { Observable } from "rxjs/internal/Observable";

@Injectable({
  providedIn: 'root'
})


export class AuthService {

  private pathService = 'api/auth';
  private logPrefix: String = "users-services ";

  constructor(
    private myUserSvcLog: MyLoggingService,
    private httpClient: HttpClient) {  }

  //login
  public getByString(string: String): Observable<UserInfo> {
    this.myUserSvcLog.info(this.logPrefix + "getByString("+string+")");
    return this.httpClient.get<UserInfo>(this.pathService + '/' + string );
  }

  //
  public create(userInfo: UserInfo): Observable<UserInfo> {
    this.myUserSvcLog.info(this.logPrefix + "create("+userInfo.username+")");
    return this.httpClient.post<UserInfo>(this.pathService, userInfo);
  }


  
}