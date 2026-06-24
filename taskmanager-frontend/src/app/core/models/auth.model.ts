export type UserRole = 'ADMIN' | 'MANAGER' | 'MEMBER' | 'MEMBER_MANAGER';
export type EntityStatus = 'INACTIVE' | 'ACTIVE' | 'DELETED';

export interface LoginPayload {
  email: string;
  password?: string;
}

export interface AuthResponse {
  token: string;
  refreshToken: string;
}

export interface RegisterPayload {
  name: string;
  email: string;
  password?: string;
  role: UserRole;
}

export interface User{
  id: string;
  name: string;
  email: string;
  role: UserRole;
  entityStatus: EntityStatus
}

export interface RegisterResponse {
  id: string;
  data: {
    user: User;
    tokenResponse: AuthResponse;
  };
  message: string;
}

export interface JwtPayload {
  sub: string;        
  roles: UserRole;    
  email: string;
  exp: number;      
}