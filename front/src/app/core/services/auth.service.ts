import { Injectable } from "@angular/core";
import { MyLoggingService } from "./logging.services";
import { HttpClient, HttpResponse } from "@angular/common/http";
import { UserInfo } from "../models/user-info.interface";
import { LoginRequest } from "../models/Login-request.interface";
import { Observable } from "rxjs/internal/Observable";
import { ServerResponse } from "../models/server-response.interface";

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
  public login(loginForm: LoginRequest): Observable<ServerResponse> {
    this.myUserSvcLog.info(this.logPrefix + "/login");
    return this.httpClient.post<ServerResponse>(this.pathService + '/login', loginForm );
  }

  //
  public create(userInfo: UserInfo): Observable<ServerResponse> {
    this.myUserSvcLog.info(this.logPrefix + "create("+userInfo.username+")");
    return this.httpClient.post<ServerResponse>(this.pathService + "/register", userInfo);
  }


  
}