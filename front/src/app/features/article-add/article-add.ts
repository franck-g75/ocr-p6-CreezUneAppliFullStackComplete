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
import { MatSelectModule } from '@angular/material/select';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { Topic } from '../../core/models/topic.interface';
import { TopicService } from '../../core/services/topic-service';
import { AsyncPipe } from '@angular/common';
import { PostService } from '../../core/services/post.service';
import { Post } from '../../core/models/post.interface';
import { HttpErrorResponse } from '@angular/common/http';
import { MatIconModule } from '@angular/material/icon';
import { BackEndErrorResponseBody } from '../../core/models/error-response.interface';

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

  private serverErrorMessage!: String;
  private serverTopicErrorMessage!: string;
  private serverTitleErrorMessage!: string;
  private serverContentErrorMessage!: string;

  constructor(
    private myLog: MyLoggingService,
    private userStore: UserStore,
    private matSnackBar: MatSnackBar,
    private formBuilder: FormBuilder,
    private router: Router,
    private topicService: TopicService,
    private postService: PostService
  ){}

  public ngOnInit(): void {

    //to add the topics in topic list items
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
          [Validators.required, Validators.maxLength(20)]
        ],
        content: ['',
          [Validators.required, Validators.maxLength(20)]
        ]
      });
  }


  public submit(): void{
    this.myLog.info(this.logPrefix + "appel du service");

    if (this.addForm.valid){

      //construction du post
      let post: Post = { id:0, 
                        title:this.addForm.get("title")?.value , 
                        content: this.addForm.get("content")?.value, 
                        id_topic: this.selected, 
                        username: this.userStore.getUsername(), 
                        created_at: new Date()};

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
          this.serverError(error);
        }
      });
    }

  }


  public showClientTitleValidationError(){
    const titleControl = this.addForm.get('title');
    if (titleControl?.touched && !titleControl?.valid) {
      if (titleControl?.errors?titleControl.errors['required']:false){
        return this.labels.mandatoryTitle;
      } else if (titleControl?.errors ? titleControl.errors['maxlength'] : false){
        return this.labels.titleMaxLength;
      } else {
        return "";
      }
    } else {
      return "";
    }
  }

  public showClientContentValidationError(){
    const contentControl = this.addForm.get('content');
    if (contentControl?.touched && !contentControl?.valid) {
      if (contentControl?.errors?contentControl.errors['required']:false){
        return this.labels.mandatoryContent;
      } else if (contentControl?.errors ? contentControl.errors['maxlength'] : false){
        return this.labels.contentMaxLength;
      } else {
        return "";
      }
    } else {
      return "";
    }
  }

  public showServerTopicValidationError(): string {
    return this.serverTopicErrorMessage!==undefined ? this.serverTopicErrorMessage : '';
  }

  public showServerTitleValidationError(): string {
    return this.serverTitleErrorMessage!==undefined ? this.serverTitleErrorMessage : '';
  }

  public showServerContentValidationError(): string {
    return this.serverContentErrorMessage!==undefined ? this.serverContentErrorMessage : '';
  }

  public showServerErrorMessage(){
    return this.serverErrorMessage!==null ? this.serverErrorMessage : '';
  }


  public  serverError(error : HttpErrorResponse): void{
      const backEndResponseBody = error.error as BackEndErrorResponseBody;
      if (backEndResponseBody!==null && backEndResponseBody!==undefined){
        this.serverTitleErrorMessage = "";
        this.serverContentErrorMessage = "";
        this.serverTopicErrorMessage = "";
        this.serverErrorMessage = "";
        if (error.status === 0) {
          console.error('Network error:', error.error)
          //this.serverErrorMessage.push("erreur réseau");
        } else {
          if (backEndResponseBody.validationErrors!==null && backEndResponseBody.validationErrors !==undefined ) {
            this.myLog.error(`Backend returned code ${error.status}, body:`, error.error);
            if (backEndResponseBody.validationErrors.topic!==undefined) {
              this.myLog.error(`Backend returned ${backEndResponseBody.validationErrors.topic}`);
              this.serverTopicErrorMessage = "le champ theme : " + backEndResponseBody.validationErrors.topic;        
            }
            if (backEndResponseBody.validationErrors.title!==undefined) {
              this.myLog.error(`Backend returned ${backEndResponseBody.validationErrors.title}`);
              this.serverTitleErrorMessage = "Le champ titre : " + backEndResponseBody.validationErrors.title;        
            }
            if (backEndResponseBody.validationErrors.content!==undefined) {
              this.myLog.error(`Backend returned ${backEndResponseBody.validationErrors.content}`);
              this.serverContentErrorMessage = "Le champ contenu : " + backEndResponseBody.validationErrors.content;
            }
          } else { //validationErrrors==nuul 
            this.myLog.error(`Backend returned ${backEndResponseBody.errorMessage}`);
            this.serverErrorMessage = "Le serveur a répondu " + backEndResponseBody.errorMessage;
          }
        }
      } else {
        this.serverTitleErrorMessage = "";
        this.serverContentErrorMessage = "";
        this.serverTopicErrorMessage = "";
        this.serverErrorMessage = "";
      }
    }

}
