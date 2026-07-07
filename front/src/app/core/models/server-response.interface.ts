import { BackEndErrorResponseBody } from "./error-response.interface";
import { SessionInfo } from "./session-info.interface";

export interface ServerResponse {
  message: string;
  code: string;
  data: string | BackEndErrorResponseBody | SessionInfo;
}