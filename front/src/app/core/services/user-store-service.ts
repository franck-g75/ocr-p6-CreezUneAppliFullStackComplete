import { Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UserStore {


  username = signal('');
  setUsername(username: string): void {
    this.username.set(username);
  }

  email = signal('');
  setEmail(email: string): void {
    this.email.set(email);
  }
}