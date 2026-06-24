import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { environment } from '../../../environments/environment';
import { User } from '../models/auth.model';
import { Observable } from 'rxjs';
import { ApiResponse, PaginatedResponse } from '../models/response.model';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private http = inject(HttpClient);
  private readonly API_URL = environment.apiUrl;

  users = signal<User[]>([]);

  getUserByEmail(email: string): Observable<ApiResponse<User>> {
    return this.http.get<ApiResponse<User>>(
      `${this.API_URL}/user/email/${email}`,
    );
  }

  getAllUsers(
    page: number = 0,
    size: number = 20,
  ): Observable<PaginatedResponse<User>> {
    return this.http.get<PaginatedResponse<User>>(`${this.API_URL}/user`, {
      params: { page: page.toString(), size: size.toString() },
    });
  }
}
