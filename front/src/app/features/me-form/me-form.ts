import { Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ME_LABELS } from '../../shared/labels';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { Topic } from '../../core/models/topic.interface';
import { TopicService } from '../../core/services/topic.service';
import { UserStore } from '../../core/services/user-store.service';
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

  constructor(
    private topicService: TopicService,
    private userStore: UserStore,
  ) {  }

  public ngOnInit(): void { 
    
    this.idUser = this.userStore.getUserId();

    this.topicService.all(this.userStore.getUsername()).subscribe(
      topics => {
        this.topicSubject.next(topics);
      });

  }

}



