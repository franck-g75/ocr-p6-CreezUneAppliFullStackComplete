import { Component, Input, OnInit } from '@angular/core';
import { MyLoggingService } from '../../core/services/logging.services';
import { ActivatedRoute } from '@angular/router';
import { ARTICLE_LABELS } from '../../shared/labels';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { Post } from '../../core/models/post.interface';
import { PostService } from '../../core/services/post.service';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { AsyncPipe, DatePipe } from '@angular/common';
import { Comment } from '../../core/models/comment.interface';
import { MatCardModule } from '@angular/material/card';
import { MatFormField, MatFormFieldModule, MatLabel } from '@angular/material/form-field';
import { UserStore } from '../../core/services/user-store-service';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { HttpErrorResponse } from '@angular/common/http';
import { BackEndErrorResponseBody } from '../../core/models/error-response.interface';
import { MatInputModule } from '@angular/material/input';
@Component({
  selector: 'app-article-details',
  imports: [FormsModule, ReactiveFormsModule , DatePipe, AsyncPipe, MatInputModule, MatIconModule, MatButtonModule, MatButtonModule, MatCardModule, MatLabel, MatFormFieldModule, MatSnackBarModule],
  templateUrl: './article-details.html',
  styleUrl: './article-details.scss',
})
export class ArticleDetails implements OnInit{

  public labels = ARTICLE_LABELS;

  private onError: boolean = false;
  private logPrefix: String = "articleDetails - ";

  public addComment!: FormGroup;

  private commentSubject = new BehaviorSubject<Comment[]>([]);
  public comment$ = this.commentSubject.asObservable();

  public post?: Post;
  @Input({required:true}) public idArticle!: number;

  private serverContentErrorMessage!: string;
  private serverErrorMessage!: String;

  constructor(
      private myLog: MyLoggingService,
      private route: ActivatedRoute,
      private formBuilder: FormBuilder,
      private postService: PostService,
      private matSnackBar: MatSnackBar,
      private userStore: UserStore,
    ){}

  public ngOnInit(){
    this.idArticle = Number(this.route.snapshot.paramMap.get('idArticle'));
    this.myLog.info(this.logPrefix + " idArticle = " + this.idArticle);
    
    this.postService.getPost(this.idArticle).subscribe(
      post => {
         this.post = post;
      });
    
      this.getComments();

      this.initForm();

  }

  public initForm(){

      this.addComment = this.formBuilder.group({
        content: ['',
          [Validators.required, Validators.maxLength(2000)]
        ]
      });


  }
    
  private getComments(){
    this.postService.getComments(this.idArticle).subscribe(
      comments => {
        this.commentSubject.next(comments);
      }
    );
  }

  public back() {
    window.history.back();
  }
  

  showServerErrorMessage(){
    return this.serverErrorMessage!==null ? this.serverErrorMessage : '';
  }

  showServerCommentValidationError(){
    return this.serverContentErrorMessage!==undefined ? this.serverContentErrorMessage : '';
  }

  showClientCommentValidationError(){
    const contentControl = this.addComment.get('content');
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

  submit(){
    this.myLog.info(this.logPrefix + "Submit addForm : validation form");

    if (this.addComment.valid){
      //construction du post
      let comment: Comment = { 
        id:0, 
        content: this.addComment.get("content")?.value, 
        username: this.userStore.getUsername(), 
      };

      this.postService.addComment(this.idArticle,comment).subscribe({
        next: (response: Comment) => {
          this.myLog.info(this.logPrefix + " add Réalisé " );
          this.onError = false;
          this.matSnackBar.open("ADD OK", 'Close', { duration: 3000 });
          this.getComments();
          const contentControl = this.addComment.get('content');
          contentControl?.reset();
        },
        error : (error: HttpErrorResponse) => {
          this.myLog.info(this.logPrefix + "add post error : " + error.error);
          this.onError = true;
          this.serverError(error);
        }
      });
    
    }
  
  }

  
  serverError(error: HttpErrorResponse): void{
        const backEndResponseBody = error.error as BackEndErrorResponseBody;
        if (backEndResponseBody!==null && backEndResponseBody!==undefined){
          this.serverContentErrorMessage = "";
          if (error.status === 0) {
            console.error('Network error:', error.error)
            //this.serverErrorMessage.push("erreur réseau");
          } else {
            if (backEndResponseBody.validationErrors!==null && backEndResponseBody.validationErrors !==undefined ) {
              this.myLog.error(`Backend returned code ${error.status}, body:`, error.error);
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
          this.serverContentErrorMessage = "";
          this.serverErrorMessage = "";
        }
      }

}
