export interface LoginRequest {
  emailAddress: string;
  password: string;
}

export interface AuthToken {
  accessToken: string;
}