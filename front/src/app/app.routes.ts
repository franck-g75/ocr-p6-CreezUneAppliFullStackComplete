import { Routes } from '@angular/router';
import { NotFound } from './features/not-found/not-found';
import { LoginForm } from './features/login-form/login-form';
import { LandingPage } from './features/landing-page/landing-page';
import { SubscriptionForm } from './features/subscription-form/subscription-form';
import { MeForm } from './features/me-form/me-form';
import { ArticleHome } from './features/article-home/article-home';
import { ArticleAdd } from './features/article-add/article-add';
import { Theme } from './features/theme/theme';
import { ArticleDetails } from './features/article-details/article-details';

export const routes: Routes = [
  { path: '',  redirectTo: 'landing', pathMatch:'full'},
  { path: 'landing',  component: LandingPage},
  { path: 'subscription', component: SubscriptionForm},
  { path: 'login', component: LoginForm},
  { path: 'article', component: ArticleHome},
  { path: 'add-article', component: ArticleAdd},
  { path: 'article-details/:idArticle', component: ArticleDetails},
  { path: 'theme', component: Theme},
  { path: 'me', component: MeForm},
  { path: '**', component: NotFound}
];
