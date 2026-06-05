import { Component } from '@angular/core';
import { TopicService } from '../../core/services/topic-service';
import { Observable } from 'rxjs/internal/Observable';
import { Topic } from '../../core/models/topic.interface';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { AsyncPipe, LowerCasePipe, UpperCasePipe } from '@angular/common';
import { MatButton } from '@angular/material/button';

@Component({
  selector: 'app-topic',
  imports: [UpperCasePipe, LowerCasePipe, AsyncPipe, MatButton],
  templateUrl: './topic-form.html',
  styleUrl: './topic-form.scss',
})
export class TopicForm {
  
  public topic$: Observable<Topic[]> = new BehaviorSubject<Topic[]>([]);

  constructor(
    private topicService: TopicService
  ) {  }

  public ngOnInit(): void {
    
    this.topic$ = this.topicService.all();

  }
}
