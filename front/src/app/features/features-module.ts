import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TopicForm } from './topic-form/topic-form';

@NgModule({
  declarations: [],
  imports: [
    CommonModule, TopicForm
  ],
  exports: [ TopicForm ]
})
export class FeaturesModule { }
