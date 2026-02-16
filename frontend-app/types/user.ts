export interface CreateUserRequest {
  firstName: string;
  lastName: string;
  emailAddress: string;
  password: string;
}

export interface UserResponse {
  id: string;
  firstName: string;
  lastName: string;
  emailAddress: string;
}