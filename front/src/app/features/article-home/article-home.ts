import { Component } from '@angular/core';
import { MatIconModule } from "@angular/material/icon";
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { Router } from '@angular/router';
import { ARTICLE_LABELS, GENERIC_LABELS } from '../../shared/labels';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { AsyncPipe, DatePipe } from '@angular/common';
import { PostService } from '../../core/services/post.service';
import { Post } from '../../core/models/post.interface';
import { ShortenPipe } from "../../shared/shorten.pipe";
import { MyLoggingService } from '../../core/services/logging.services';
import { SessionService } from '../../core/services/session.service';
import { BackEndPostArray } from '../../core/models/back-end-post-array.interface';
import { ServerResponse } from '../../core/models/server-response.interface';
import { HttpErrorResponse } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';


@Component({
  selector: 'app-article-home',
  imports: [MatIconModule, MatCardModule, MatButtonModule, AsyncPipe, DatePipe, ShortenPipe],
  templateUrl: './article-home.html',
  styleUrl: './article-home.scss',
})
export class ArticleHome {

  public labels = ARTICLE_LABELS;
  public labelsGeneric = GENERIC_LABELS;
  private postSubject = new BehaviorSubject<Post[]>([]);
  public post$ = this.postSubject.asObservable();
  public descSort: boolean = true;
  public idUser!: number;
  private logPrefix: string = "ArticleHome ";

  public constructor(
    private myLog: MyLoggingService,
    private sessionService: SessionService,
    private router: Router, 
    private postService: PostService,
    private matSnackBar: MatSnackBar
  ){}

  public ngOnInit(): void { 
      
      this.myLog.debug("article-home.ngOnInit");
      this.idUser = this.sessionService.sessionInformation ? this.sessionService.sessionInformation.id : 0;

      if (this.idUser>0){
        this.postService.all().subscribe({

          next: (response: ServerResponse) => {
              this.myLog.info(this.logPrefix + "  searching all posts in response" );

              try{
                let bePosts = response.data as BackEndPostArray;
                this.postSubject.next(bePosts.posts);
                } catch(e: any) {
                  this.myLog.error(this.logPrefix + "  all posts in error");
                }
                this.myLog.info(this.logPrefix + "  all posts found" );

              },
              
          error: (error: HttpErrorResponse) => {
              this.myLog.info(this.logPrefix + " get all posts error : " + error.status.toString() + " " + error.statusText);
          
              this.matSnackBar.open(
                this.labelsGeneric.error + error.status.toString() + " " + error.statusText, 'Close', { duration: 3000 }
              );
            }
            
        });
    } else {
      
      this.matSnackBar.open(this.labelsGeneric.msgCnxKo, 'Close', { duration: 3000 });
      this.router.navigate(['landing']);

    }
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
