import { Component } from '@angular/core';
import { TopicModule } from '../../shared/topic/topic';

@Component({
  selector: 'app-theme',
  imports: [TopicModule],
  templateUrl: './theme.html',
  styleUrl: './theme.scss',
})
export class Theme {

}
