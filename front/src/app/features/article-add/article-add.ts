import { Component } from '@angular/core';
import { ARTICLE_LABELS, GENERIC_LABELS } from '../../shared/labels';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { Router } from '@angular/router';
import { MyLoggingService } from '../../core/services/logging.services';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatSelectModule } from '@angular/material/select';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { Topic } from '../../core/models/topic.interface';
import { TopicService } from '../../core/services/topic.service';
import { AsyncPipe } from '@angular/common';
import { PostService } from '../../core/services/post.service';
import { Post } from '../../core/models/post.interface';
import { HttpErrorResponse } from '@angular/common/http';
import { MatIconModule } from '@angular/material/icon';
import { BackEndErrorResponseBody } from '../../core/models/error-response.interface';
import { SessionService } from '../../core/services/session.service';
import { ServerResponse } from '../../core/models/server-response.interface';
import { BackEndTopicArray } from '../../core/models/back-end-topic-array.interface';

@Component({
  selector: 'app-article-add',
  imports: [FormsModule, ReactiveFormsModule, AsyncPipe,  MatButtonModule, MatCardModule, MatInputModule, MatSelectModule, MatIconModule],
  templateUrl: './article-add.html',
  styleUrl: './article-add.scss',
})
export class ArticleAdd {

  public labels = ARTICLE_LABELS;
  public labels_generic = GENERIC_LABELS;

  private userName!: string;

  public addForm!: FormGroup;
  private logPrefix: String = "addArticle - ";

  private topicSubject = new BehaviorSubject<Topic[]>([]);
  public topic$ = this.topicSubject.asObservable();
  public selected: number = 0;

  private serverErrorMessage!: String;
  private serverTopicErrorMessage!: string;
  private serverTitleErrorMessage!: string;
  private serverContentErrorMessage!: string;

  constructor(
    private myLog: MyLoggingService,
    private sessionService: SessionService,
    private matSnackBar: MatSnackBar,
    private formBuilder: FormBuilder,
    private router: Router,
    private topicService: TopicService,
    private postService: PostService
  ){}

  public ngOnInit(): void {

    this.myLog.debug("article-home.ngOnInit");
    this.userName = this.sessionService.sessionInformation ? this.sessionService.sessionInformation.username : '';

    if (this.userName!=null && this.userName!=''){

      //to add the topics in topic list items
      this.topicService.allTopics().subscribe({
        next: (response: ServerResponse) => {

          try{
            let beTopic = response.data as BackEndTopicArray;

            this.topicSubject.next(beTopic.topics);
            this.selected = beTopic.topics[0].id;
            this.myLog.info('loading of topic list done.');

          } catch(e: any) {
            this.myLog.error(this.logPrefix + "  all topics in error");
          }

        },
        error: (error: HttpErrorResponse) => {
          this.myLog.info('srv error found ... ' + error); 
          this.matSnackBar.open(this.labels_generic.error + " " + error.status + " " + error.statusText, 'Close', { duration: 3000 });
        }
      });

    } else {

      this.matSnackBar.open(this.labels_generic.error, 'Close', { duration: 3000 });
      this.router.navigate(['landing']);

    }

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
          [Validators.required, Validators.maxLength(30)]
        ],
        content: ['',
          [Validators.required, Validators.maxLength(4000)]
        ]
      });
  }


  public submit(): void{
    this.myLog.info(this.logPrefix + "Submit addForm : validation form");

    if (this.addForm.valid){

      //construction du post
      let post: Post = { id:0, 
                        title:this.addForm.get("title")?.value , 
                        content: this.addForm.get("content")?.value, 
                        id_topic: this.selected, 
                        topic_title: "",
                        username: this.userName, 
                        created_at: new Date()};

      this.postService.addPost(post).subscribe({
        next: (response: ServerResponse) => {
          this.myLog.info(this.logPrefix + " add Réalisé " );
          this.matSnackBar.open(response.code, 'Close', { duration: 3000 });
          this.router.navigate(['article']);
        },
        error : (error: HttpErrorResponse) => {
          this.myLog.info(this.logPrefix + "add post error : " + error.error);
          this.serverError(error);
        }
      });
    } else {
      this.myLog.error("erreurs dans le formulaire...");
      this.matSnackBar.open(this.labels_generic.clientError, 'Close', { duration: 10000 });
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
            if (backEndResponseBody.validationErrors.topic_id!==undefined) {
              this.myLog.error(`Backend returned ${backEndResponseBody.validationErrors.topic_id}`);
              this.serverTopicErrorMessage = backEndResponseBody.validationErrors.topic_id;        
            }
            if (backEndResponseBody.validationErrors.topic_title!==undefined) {
              this.myLog.error(`Backend returned ${backEndResponseBody.validationErrors.topic_title}`);
              this.serverTopicErrorMessage = backEndResponseBody.validationErrors.topic_title;        
            }
            if (backEndResponseBody.validationErrors.title!==undefined) {
              this.myLog.error(`Backend returned ${backEndResponseBody.validationErrors.title}`);
              this.serverTitleErrorMessage = backEndResponseBody.validationErrors.title;        
            }
            if (backEndResponseBody.validationErrors.content!==undefined) {
              this.myLog.error(`Backend returned ${backEndResponseBody.validationErrors.content}`);
              this.serverContentErrorMessage = backEndResponseBody.validationErrors.content;
            }
          } else { //validationErrrors==null 
            this.myLog.error(`Backend returned ${backEndResponseBody.errorMessage}`);
            this.serverErrorMessage = "Le serveur a répondu : " + backEndResponseBody.errorMessage;
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
