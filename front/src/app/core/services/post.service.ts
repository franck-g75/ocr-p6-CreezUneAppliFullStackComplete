import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { MyLoggingService } from "./logging.services";
import { Post } from "../models/post.interface";
import { Observable } from "rxjs/internal/Observable";

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
      this.myLog.info("PostService path = " + this.pathService + "/" + userid);
      return this.httpClient.get<Post[]>(this.pathService + "/" + userid);
    }

    public addPost(post: Post){
      this.myLog.info("PostService path = " + this.pathService );
      return this.httpClient.post<Post>(this.pathService, post);
    } 
}