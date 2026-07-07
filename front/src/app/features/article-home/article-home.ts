import { Component } from '@angular/core';
import { MatIconModule } from "@angular/material/icon";
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { Router } from '@angular/router';
import { ARTICLE_LABELS } from '../../shared/labels';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { AsyncPipe, DatePipe } from '@angular/common';
import { PostService } from '../../core/services/post.service';
import { Post } from '../../core/models/post.interface';
import { ShortenPipe } from "../../shared/shorten.pipe";
import { MyLoggingService } from '../../core/services/logging.services';
import { SessionService } from '../../core/services/session.service';


@Component({
  selector: 'app-article-home',
  imports: [MatIconModule, MatCardModule, MatButtonModule, AsyncPipe, DatePipe, ShortenPipe],
  templateUrl: './article-home.html',
  styleUrl: './article-home.scss',
})
export class ArticleHome {

  public labels = ARTICLE_LABELS;
  private postSubject = new BehaviorSubject<Post[]>([]);
  public post$ = this.postSubject.asObservable();
  public descSort: boolean = true;
  public idUser!: number;
  
  public constructor(
    private myLog: MyLoggingService,
    private sessionService: SessionService,
    private router: Router, 
    private postService: PostService,

  ){}

  public ngOnInit(): void { 
      
      this.myLog.debug("article-home.ngOnInit");
      this.idUser = this.sessionService.sessionInformation ? this.sessionService.sessionInformation.id : 0;

      this.postService.all(this.idUser).subscribe(
        posts => {
          this.postSubject.next(posts);
        });
      
    }

  public addArticle() {
    this.router.navigate(['add-article']);
  }

  public sort(){
    this.descSort = !this.descSort;
    this.post$.subscribe((posts: Post[]) => {
      
      let len: number = posts.length;
      let half: number = len/2;
      let i: number = 0;
      if (len>1){
        for (i=0; i<half; i++){
          let post: Post = posts[i];
          posts[i]=posts[len-i-1];
          posts[len-i-1]=post;
        }
      }
      
    });

  }

  public showArticleDetails(idArticle: number){

    this.router.navigate(['/article-details',idArticle]);

  }
  

}
