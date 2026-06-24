import { inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {
  Project,
  CreateProjectPayload,
  AddMemberPayload,
  AddMember,
  ProjectReport,
} from '../models/project.model';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { PaginatedResponse, ApiResponse } from '../models/response.model';

@Injectable({
  providedIn: 'root',
})
export class ProjectService {
  private http = inject(HttpClient);
  private readonly API_URL = environment.apiUrl;

  currentProject = signal<Project | null>(null);
  userProjects = signal<Project[]>([]);

  getProjectsByMember(
    memberId: string,
  ): Observable<PaginatedResponse<Project>> {
    return this.http
      .get<
        PaginatedResponse<Project>
      >(`${this.API_URL}/project/member/${memberId}`)
      .pipe(
        tap((res) => {
          this.userProjects.set(res.content);
          if (res.content.length > 0 && !this.currentProject()) {
            this.currentProject.set(res.content[0]);
          }
        }),
      );
  }

  createProject(
    payload: CreateProjectPayload,
  ): Observable<ApiResponse<Project>> {
    return this.http
      .post<ApiResponse<Project>>(`${this.API_URL}/project`, payload)
      .pipe(
        tap((res) => {
          this.userProjects.update((prev) => [...prev, res.data]);
          this.currentProject.set(res.data);
        }),
      );
  }

  addProjectMember(
    payload: AddMemberPayload,
  ): Observable<ApiResponse<AddMember>> {
    return this.http.post<ApiResponse<AddMember>>(
      `${this.API_URL}/members/add`,
      payload,
    );
  }

  getAllProjects(
    page: number = 0,
    size: number = 20,
  ): Observable<PaginatedResponse<Project>> {
    return this.http.get<PaginatedResponse<Project>>(
      `${this.API_URL}/project`,
      {
        params: { page: page.toString(), size: size.toString() },
      },
    );
  }

  getProjectReport(projectId: string): Observable<ApiResponse<ProjectReport>> {
    return this.http.get<ApiResponse<ProjectReport>>(
      `${this.API_URL}/projects/${projectId}/report`,
    );
  }
}
