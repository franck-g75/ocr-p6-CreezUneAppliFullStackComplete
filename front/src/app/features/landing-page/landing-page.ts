import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { LANDING_LABELS } from '../../shared/labels';

@Component({
  selector: 'app-landing-page',
  imports: [ RouterLink],
  templateUrl: './landing-page.html',
  styleUrl: './landing-page.scss',
})

export class LandingPage {
  labels = LANDING_LABELS;

}