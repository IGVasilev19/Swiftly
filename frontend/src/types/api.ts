import { AxiosError } from "axios";

export type ApiSuccessResponse = {
  success: true;
  message: string;
};

export type ApiErrorResponse<T = unknown> = {
  success: false;
  errorCode: string;
  statusCode: number;
  message: string;
  userMessage: string;
  data?: T;
  [key: string]: unknown;
} & Record<string, unknown>;

export type ApiResponse<E = unknown> = ApiSuccessResponse | ApiErrorResponse<E>;

export type ApiError<T = unknown> = AxiosError<ApiErrorResponse<T>>;
