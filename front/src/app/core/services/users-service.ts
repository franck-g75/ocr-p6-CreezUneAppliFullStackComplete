import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Users } from '../models/users.interface';

@Injectable({
  providedIn: 'root'
})
export class UsersService {

  private pathService = 'api/users';

  constructor(private httpClient: HttpClient) {
  }

  public getByString(string: String): Observable<Users> {
    return this.httpClient.get<Users>(this.pathService + '/' + string );
  }
  
}