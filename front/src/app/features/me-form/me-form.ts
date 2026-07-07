import { Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ME_LABELS } from '../../shared/labels';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { Topic } from '../../core/models/topic.interface';
import { MatInputModule } from '@angular/material/input'
import { FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SignupForm } from "../../shared/signup-form/signup-form";
import { TopicModule } from "../../shared/topic/topic";


@Component({
  selector: 'app-me-form',
  imports: [FormsModule, ReactiveFormsModule, MatInputModule, SignupForm, TopicModule],
  templateUrl: './me-form.html',
  styleUrl: './me-form.scss',
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  standalone: true
})
export class MeForm {

  public labels = ME_LABELS;
  public myOrigin = "me";

  public hide: boolean = true;
  public onError: boolean = false;
  public myLoginForm!: FormGroup;
  private topicSubject = new BehaviorSubject<Topic[]>([]);
  public topic$ = this.topicSubject.asObservable();

  public idUser!: number;

  constructor() {  }


}



