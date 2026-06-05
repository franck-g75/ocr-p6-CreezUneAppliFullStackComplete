import { Component } from '@angular/core';
import { MatButton } from '@angular/material/button';
import { RouterLink } from '@angular/router';
import { LANDING_LABELS } from '../../shared/labels';

@Component({
  selector: 'app-landing-page',
  imports: [MatButton, RouterLink],
  templateUrl: './landing-page.html',
  styleUrl: './landing-page.scss',
})

export class LandingPage {
  labels = LANDING_LABELS;

}