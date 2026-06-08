import { Component } from '@angular/core';
import { TopicService } from '../../core/services/topic-service';
import { Observable } from 'rxjs/internal/Observable';
import { Topic } from '../../core/models/topic.interface';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { AsyncPipe, LowerCasePipe, UpperCasePipe } from '@angular/common';
import { MatButton } from '@angular/material/button';
import { UserStore } from '../../core/services/user-store-service';
import { TOPIC_LABELS } from '../../shared/labels';


@Component({
  selector: 'app-topic',
  imports: [UpperCasePipe, LowerCasePipe, AsyncPipe, MatButton],
  templateUrl: './topic-form.html',
  styleUrl: './topic-form.scss',
})
export class TopicForm {
  
  public topic$: Observable<Topic[]> = new BehaviorSubject<Topic[]>([]);
  public labels = TOPIC_LABELS;

  constructor(
    private topicService: TopicService,
    private userStore: UserStore
  ) {  }

  public ngOnInit(): void {
    
    this.topic$ = this.topicService.all(this.userStore.getUsername());

  }
}
