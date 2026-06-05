import { Routes } from '@angular/router';
import { NotFound } from './features/not-found/not-found';
import { TopicForm } from './features/topic-form/topic-form';
import { LoginForm } from './features/login-form/login-form';
import { LandingPage } from './features/landing-page/landing-page';

export const routes: Routes = [
  {
    path: '',
    component: LandingPage,
  },{
    path: 'login',
    component: LoginForm
  },{
    path: 'topic',
    component: TopicForm,
  },{
    path: '**', // wildcard
    component: NotFound,
  }
];
