import { Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UserStore {
//TODO: Averifier...

  username = signal('');
  setUsername(username: string): void {
    this.username.set(username);
  }
  getUsername(): string {
    return this.username();
  }

  userid = signal(0);
  setUserId(userid: number): void {
    this.userid.set(userid);
  }
  getUserId(): number {
    return this.userid();
  }

/*
  username: String = '';
  setUsername(username: string): void {
    this.username=username;
  }
*/
  email: String = '';;
  setEmail(email: string): void {
    this.email=email;
  }
}