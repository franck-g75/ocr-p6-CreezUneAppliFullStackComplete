import { Routes } from '@angular/router';
import { NotFound } from './features/not-found/not-found';
import { TopicForm } from './features/topic-form/topic-form';
import { LoginForm } from './features/login-form/login-form';
import { LandingPage } from './features/landing-page/landing-page';
import { SubscriptionForm } from './features/subscription-form/subscription-form';
import { MeForm } from './features/me-form/me-form';

export const routes: Routes = [
  { path: '',  redirectTo: 'landing', pathMatch:'full'},
  { path: 'landing',  component: LandingPage},
  { path: 'subscription', component: SubscriptionForm},
  { path: 'login', component: LoginForm},
  { path: 'topic', component: TopicForm},
  { path: 'me', component: MeForm},
  { path: '**', component: NotFound}
];
