import { inject, Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { tap, Observable } from 'rxjs';
import { LoginPayload, AuthResponse, RegisterPayload, RegisterResponse, UserRole, JwtPayload } from '../models/auth.model';
import { jwtDecode } from 'jwt-decode';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);
  private readonly API_URL = environment.apiUrl;

  #token = signal<string | null>(localStorage.getItem('token'));
  #refreshToken = signal<string | null>(localStorage.getItem('refreshToken'));
  
  isAuthenticated = computed(() => !!this.#token());

  currentUserData = computed<JwtPayload | null>(() => {
    const currentToken = this.#token();
    if (!currentToken) return null;
    try {
      return jwtDecode<JwtPayload>(currentToken);
    } catch {
      return null;
    }
  });

  userId = computed(() => this.currentUserData()?.sub ?? null);
  userRole = computed(() => this.currentUserData()?.roles ?? null);
  userEmail = computed(() => this.currentUserData()?.email ?? null)

  get token() { return this.#token(); }
  get refreshToken() { return this.#refreshToken(); }

  login(payload: LoginPayload): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/auth/login`, payload).pipe(
      tap(response => this.handleAuthentication(response.token, response.refreshToken))
    );
  }

  register(payload: RegisterPayload): Observable<RegisterResponse> {
    return this.http.post<RegisterResponse>(`${this.API_URL}/user`, payload).pipe(
      tap(response => {
        const tokens = response.data.tokenResponse;
        this.handleAuthentication(tokens.token, tokens.refreshToken);
      })
    );
  }

  refreshSession(): Observable<AuthResponse> {
    const currentRefresh = this.#refreshToken();
    return this.http.post<AuthResponse>(`${this.API_URL}/auth/refresh`, { refreshToken: currentRefresh }).pipe(
      tap({
        next: response => this.handleAuthentication(response.token, response.refreshToken),
        error: () => this.logout()
      })
    );
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
    this.#token.set(null);
    this.#refreshToken.set(null);
    this.router.navigate(['/login']);
  }

  private handleAuthentication(token: string, refreshToken: string): void {
    localStorage.setItem('token', token);
    localStorage.setItem('refreshToken', refreshToken);
    
    this.#token.set(token);
    this.#refreshToken.set(refreshToken);
    
    this.router.navigate(['/tasks']);
  }
}