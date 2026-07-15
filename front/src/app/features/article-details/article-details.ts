import { Component, Input, OnInit } from '@angular/core';
import { MyLoggingService } from '../../core/services/logging.services';
import { ActivatedRoute, Router } from '@angular/router';
import { ARTICLE_LABELS, GENERIC_LABELS } from '../../shared/labels';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { Post } from '../../core/models/post.interface';
import { PostService } from '../../core/services/post.service';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { AsyncPipe, DatePipe } from '@angular/common';
import { Comment } from '../../core/models/comment.interface';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule, MatLabel } from '@angular/material/form-field';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { HttpErrorResponse } from '@angular/common/http';
import { BackEndErrorResponseBody } from '../../core/models/error-response.interface';
import { MatInputModule } from '@angular/material/input';
import { SessionService } from '../../core/services/session.service';
import { ServerResponse } from '../../core/models/server-response.interface';
import { BackEndPost } from '../../core/models/back-end-post.interface';
import { BackEndCommentsArray } from '../../core/models/back-en-comment-array.interface';
@Component({
  selector: 'app-article-details',
  imports: [FormsModule, ReactiveFormsModule , DatePipe, AsyncPipe, MatInputModule, MatIconModule, MatButtonModule, MatButtonModule, MatCardModule, MatLabel, MatFormFieldModule, MatSnackBarModule],
  templateUrl: './article-details.html',
  styleUrl: './article-details.scss',
})
export class ArticleDetails implements OnInit{

  public labels = ARTICLE_LABELS;
  public labelsGeneric = GENERIC_LABELS;

  private logPrefix: String = "articleDetails - ";
  private idUser!: number;
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
      private router: Router,
      private sessionService: SessionService
    ){}

  public ngOnInit(){

    this.idArticle = Number(this.route.snapshot.paramMap.get('idArticle'));
    this.myLog.info(this.logPrefix + " idArticle = " + this.idArticle);
    this.idUser = this.sessionService.sessionInformation ? this.sessionService.sessionInformation.id : 0;
    this.myLog.info(this.logPrefix + " idUser = " + this.idUser); 

    if (this.idUser>0){
      this.postService.getPost(this.idArticle).subscribe({
        next: (response: ServerResponse) => {
          this.myLog.info(this.logPrefix + "  searching post in response" );

          try{

              let bePost = response.data as BackEndPost;
              this.post = bePost.post;

            } catch(e: any) {
              this.myLog.error(this.logPrefix + " post in error");
            }
            this.myLog.info(this.logPrefix + " post found");
            
        },
        error: (error: HttpErrorResponse) => {
          this.myLog.info(this.logPrefix + " get post error : " + error.status.toString() + " " + error.statusText);
      
          this.matSnackBar.open(
            this.labelsGeneric.error + error.status.toString() + " " + error.statusText, 'Close', { duration: 3000 }
          );
        }

      });
    
      this.getComments();
      this.initForm();

    } else {
      this.matSnackBar.open(this.labelsGeneric.msgCnxKo, 'Close', { duration: 3000 });
      this.router.navigate(['landing']);
    }
    

  }

  public initForm(){

      this.addComment = this.formBuilder.group({
        content: ['',
          [Validators.required, Validators.maxLength(2000)]
        ]
      });

  }
    
  private getComments(){
   
      this.postService.getComments(this.idArticle).subscribe({
        next: (response: ServerResponse) => {
          try{

            let beComments = response.data as BackEndCommentsArray;
            this.commentSubject.next(beComments.comments);

          } catch(e: any) {
              this.myLog.error(this.logPrefix + " post in error");
          }
          this.myLog.info(this.logPrefix + " post found");
        },
        error: (error: HttpErrorResponse) => {
          this.myLog.info(this.logPrefix + " get comments error : " + error.status.toString() + " " + error.statusText);
      
          this.matSnackBar.open(
            this.labelsGeneric.error + error.status.toString() + " " + error.statusText, 'Close', { duration: 3000 }
          );
        }

      });
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
        username: this.sessionService.sessionInformation ? this.sessionService.sessionInformation?.username : "", 
      };

      this.postService.addComment(this.idArticle,comment).subscribe({
        next: (response: ServerResponse) => {

          this.myLog.info(this.logPrefix + " add Réalisé " );                 //log
          this.matSnackBar.open(response.code, 'Close', { duration: 3000 });  //show success msg
          this.getComments();                                                 //get every comments 
          const contentControl = this.addComment.get('content');              //reinitialize ...
          contentControl?.reset();                                            //  the textarea

        },
        error : (error: HttpErrorResponse) => {

          this.myLog.info(this.logPrefix + "add post error : " + error.error);
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
            if (backEndResponseBody.validationErrors!==null && backEndResponseBody.validationErrors!==undefined ) {
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
