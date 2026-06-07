import { Routes } from '@angular/router';
import { NotFound } from './features/not-found/not-found';
import { TopicForm } from './features/topic-form/topic-form';
import { LoginForm } from './features/login-form/login-form';
import { LandingPage } from './features/landing-page/landing-page';
import { SubscriptionForm } from './features/subscription-form/subscription-form';

export const routes: Routes = [
  { path: '',  redirectTo: 'landing', pathMatch:'full'},
  { path: 'landing',  component: LandingPage},
  { path: 'subscription', component: SubscriptionForm},
  { path: 'login', component: LoginForm},
  { path: 'topic', component: TopicForm},
  { path: '**', component: NotFound}
];
