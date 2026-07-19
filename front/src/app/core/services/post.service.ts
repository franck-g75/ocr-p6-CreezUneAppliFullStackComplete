import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { MyLoggingService } from "./logging.services";
import { Post } from "../models/post.interface";
import { Observable } from "rxjs/internal/Observable";
import { Comment } from "../models/comment.interface";
import { ServerResponse } from "../models/server-response.interface";

@Injectable({
  providedIn: 'root'
})
export class PostService {
    private pathService = 'api/post';

    constructor(
        private httpClient: HttpClient,
        private myLog: MyLoggingService,
    ) { }

    public all(): Observable<ServerResponse> {
      this.myLog.info("PostService path = " + this.pathService + "/user" );
      return this.httpClient.get<ServerResponse>(this.pathService + "/user");
    }

    public addPost(post: Post){
      this.myLog.info("PostService path = " + this.pathService );
      return this.httpClient.post<ServerResponse>(this.pathService, post);
    } 
  
    public getPost(postid: number): Observable<ServerResponse> {
      this.myLog.info("PostService path = " + this.pathService + "/" + postid);
      return this.httpClient.get<ServerResponse>(this.pathService + "/" + postid);
    }

    public getComments(postid: number): Observable<ServerResponse> {
      this.myLog.info("PostService path = " + this.pathService + "/comments/" + postid  );
      return this.httpClient.get<ServerResponse>(this.pathService + "/comments/" + postid );
    }

    public addComment(postid: number, comment: Comment){
      this.myLog.info("PostService path = " + this.pathService + "/comment/" + postid  );
      return this.httpClient.post<ServerResponse>(this.pathService + '/comment/' + postid  , comment);
    } 
  
}