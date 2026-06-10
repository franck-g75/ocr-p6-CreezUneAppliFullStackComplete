import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, } from 'rxjs';
import { MyLoggingService } from './logging.services';
import { Users } from '../models/users.interface';

@Injectable({
  providedIn: 'root'
})
export class UsersService {

  private pathService = 'api/users';
  private logPrefix: String = "users-services ";

  constructor(
    private myLog: MyLoggingService,
    private httpClient: HttpClient) {  }
 
  public getByString(string: String): Observable<Users> {
    
    this.myLog.info(this.logPrefix + "getByString("+string+")");

    return this.httpClient.get<Users>(this.pathService + '/' + string );
    
  }

  public postsubscription(idUser: number,idTopic: number): Observable<void> { //TODO: mettre un string ok pour vérifier la bonne prise en compte du click
    
    this.myLog.info(this.logPrefix + "postsubscription(idUser="+idUser+" idTopic="+idTopic+")");

    return this.httpClient.post<void>(`${this.pathService}/idUser/${idUser}/idTopic/${idTopic}`,null);
    
  }
  
}
