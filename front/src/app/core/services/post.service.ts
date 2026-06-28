import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { MyLoggingService } from "./logging.services";
import { Post } from "../models/post.interface";
import { Observable } from "rxjs/internal/Observable";
import { Comment } from "../models/comment.interface";

@Injectable({
  providedIn: 'root'
})
export class PostService {
    private pathService = 'api/post';

    constructor(
        private httpClient: HttpClient,
        private myLog: MyLoggingService,
    ) { }

    public all(userid: number): Observable<Post[]> {
      this.myLog.info("PostService path = " + this.pathService + "/user/" + userid);
      return this.httpClient.get<Post[]>(this.pathService + "/user/" + userid);
    }

    public addPost(post: Post){
      this.myLog.info("PostService path = " + this.pathService );
      return this.httpClient.post<Post>(this.pathService, post);
    } 
  
    public getPost(postid: number): Observable<Post> {
      this.myLog.info("PostService path = " + this.pathService + "/" + postid);
      return this.httpClient.get<Post>(this.pathService + "/" + postid);
    }

    public getComments(postid: number): Observable<Comment[]> {
      this.myLog.info("PostService path = " + this.pathService + '/' + postid + "/comments" );
      return this.httpClient.get<Comment[]>(this.pathService + '/' + postid + "/comments" );
    }

    public addComment(postid: number, comment: Comment){
      this.myLog.info("PostService path = " + this.pathService + '/' + postid + "/comment" );
      return this.httpClient.post<Post>(this.pathService + '/' + postid + '/comment' , comment);
    } 
  
}