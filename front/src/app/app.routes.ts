import { Routes } from '@angular/router';
import { NotFound } from './features/not-found/not-found';
import { TopicForm } from './features/topic-form/topic-form';
import { LoginForm } from './features/login-form/login-form';
import { LandingPage } from './features/landing-page/landing-page';
import { SubscriptionForm } from './features/subscription-form/subscription-form';
import { MeForm } from './features/me-form/me-form';
import { ArticleHome } from './features/article-home/article-home';
import { ArticleShow } from './features/article-show/article-show';
import { ArticleAdd } from './features/article-add/article-add';
import { Theme } from './features/theme/theme';

export const routes: Routes = [
  { path: '',  redirectTo: 'landing', pathMatch:'full'},
  { path: 'landing',  component: LandingPage},
  { path: 'subscription', component: SubscriptionForm},
  { path: 'login', component: LoginForm},
  { path: 'article', component: ArticleHome},
  { path: 'add-article', component: ArticleAdd},
  { path: 'show-article', component: ArticleShow},
  { path: 'theme', component: Theme},
  { path: 'me', component: MeForm},
  { path: '**', component: NotFound}
];
