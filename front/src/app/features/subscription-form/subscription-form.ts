import { Component, CUSTOM_ELEMENTS_SCHEMA, OnInit } from '@angular/core';
import { SignupForm } from '../../shared/signup-form/signup-form';
import { MatIcon } from '@angular/material/icon';
import { MatIconButton } from '@angular/material/button';


@Component({
  selector: 'app-subscription-form',
  imports: [SignupForm,MatIconButton,MatIcon],
  templateUrl: './subscription-form.html',
  styleUrl: './subscription-form.scss',
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  standalone: true
})
export class SubscriptionForm {

  

  public back() {
    window.history.back();
  }
}


