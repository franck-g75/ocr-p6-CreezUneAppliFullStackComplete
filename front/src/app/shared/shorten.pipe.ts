import { Pipe, PipeTransform } from '@angular/core';
import { environment } from '../../environments/environment.prod';

@Pipe({
  name: 'shorten'
})

export class ShortenPipe implements PipeTransform {
  
  transform(value: string): string {
    if (value.length <= environment.shortentext) {
      return value;
    }
    return value.substring(0, environment.shortentext) + '…';
  }
  
}
