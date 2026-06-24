import { Component } from '@angular/core';
import { ARTICLE_LABELS } from '../../shared/labels';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { ActivatedRoute, Router } from '@angular/router';
import { MyLoggingService } from '../../core/services/logging.services';
import { UserStore } from '../../core/services/user-store-service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UserInfoService } from '../../core/services/user-info-service';
import { MatSelectModule } from '@angular/material/select';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { Topic } from '../../core/models/topic.interface';
import { TopicService } from '../../core/services/topic-service';
import { AsyncPipe } from '@angular/common';
import { PostService } from '../../core/services/post.service';
import { Post } from '../../core/models/post.interface';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-article-add',
  imports: [FormsModule, ReactiveFormsModule, AsyncPipe,  MatButtonModule, MatCardModule, MatInputModule, MatSelectModule, MatIconModule],
  templateUrl: './article-add.html',
  styleUrl: './article-add.scss',
})
export class ArticleAdd {

  public labels = ARTICLE_LABELS;
  public addForm!: FormGroup;
  private logPrefix: String = "addArticle - ";
  private onError: boolean = false;
  private topicSubject = new BehaviorSubject<Topic[]>([]);
  public topic$ = this.topicSubject.asObservable();
  public selected: number = 0;

  
  constructor(
    private myLog: MyLoggingService,
    private userStore: UserStore,
    private route: ActivatedRoute,
    private matSnackBar: MatSnackBar,
    private formBuilder: FormBuilder,
    private router: Router,
    private topicService: TopicService,
    private postService: PostService
  ){}

  public ngOnInit(): void {
    this.topicService.allTopics().subscribe(
      topics => {
        this.topicSubject.next(topics);
        this.selected = topics[0].id;
      });
    
    this.initForm();
  }

  public back() {
    window.history.back();
  }
  
  private initForm(): void {
    this.addForm = this.formBuilder.group({
        topic: ['',
          [Validators.required]
        ],
        title: ['',
          [Validators.required, Validators.maxLength(49)]
        ],
        content: ['',
          [Validators.required, Validators.maxLength(4999)]
        ]
      });
  }

  public submit(): void{
    this.myLog.info(this.logPrefix + "appel du service");
    //construction du post
    let post: Post = { id:0, 
                       title:this.addForm.get("title")?.value , 
                       content: this.addForm.get("content")?.value, 
                       id_topic: this.selected, 
                       username: this.userStore.getUsername(), 
                       created_at: new Date()};

    window.alert(post.id_topic + ' ' +  this.addForm.get("topicId")?.value + ' ' + post.content);
 
    this.postService.addPost(post).subscribe({
      next: (response: Post) => {
        this.myLog.info(this.logPrefix + " add Réalisé " );
        this.onError = false;
        this.matSnackBar.open("ADD OK", 'Close', { duration: 3000 });
        this.router.navigate(['article']);
      },
      error : (error: HttpErrorResponse) => {
        this.myLog.info(this.logPrefix + "add post error : " + error.error);
        this.onError = true;
        this.matSnackBar.open("add post error : " + error.error, 'Close', { duration: 10000 });
      }
    })
  }
}
