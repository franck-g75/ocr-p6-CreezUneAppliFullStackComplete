import { Routes } from '@angular/router';
import { NotFound } from './features/not-found/not-found';
import { TopicForm } from './features/topic-form/topic-form';

export const routes: Routes = [
  {
    path: '',
    component: TopicForm,
  },
  {
    path: '**', // wildcard
    component: NotFound,
  },];
