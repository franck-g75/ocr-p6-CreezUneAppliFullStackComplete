import { Component } from '@angular/core';
import { MatIconModule } from "@angular/material/icon";
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { Router } from '@angular/router';
import { ARTICLE_LABELS } from '../../shared/labels';
import { TopicService } from '../../core/services/topic-service';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { Topic } from '../../core/models/topic.interface';
import { UserStore } from '../../core/services/user-store-service';
import { AsyncPipe, DatePipe, LowerCasePipe, UpperCasePipe } from '@angular/common';
import { PostService } from '../../core/services/post.service';
import { Post } from '../../core/models/post.interface';
import { ShortenPipe } from "../../shared/shorten.pipe";
import { MyLoggingService } from '../../core/services/logging.services';


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
    private router: Router, 
    private postService: PostService,
    private userStore: UserStore
  ){}

  public ngOnInit(): void { 
      
      this.idUser = this.userStore.getUserId();

      this.postService.all(this.userStore.getUserId()).subscribe(
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
      
    }).unsubscribe();

  }
}
