import { BackEndCommentsArray } from "./back-en-comment-array.interface";
import { BackEndPostArray } from "./back-end-post-array.interface";
import { BackEndPost } from "./back-end-post.interface";
import { BackEndTopicArray } from "./back-end-topic-array.interface";
import { BackEndErrorResponseBody } from "./error-response.interface";
import { Post } from "./post.interface";
import { SessionInfo } from "./session-info.interface";

export interface ServerResponse {
  message: string;
  code: string;
  data: string| BackEndErrorResponseBody | SessionInfo | BackEndTopicArray | BackEndPostArray | BackEndPost | BackEndCommentsArray;
}
