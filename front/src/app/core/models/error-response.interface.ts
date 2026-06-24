import { BackEndValidationErrors } from "./back-end-validation-errors.interface";

export interface BackEndErrorResponseBody {
  errorCode: string;
  errorMessage: string;
  validationErrors: BackEndValidationErrors;
}
/*
type ValidationErrors = {
  [key: string]: any;
}
*/