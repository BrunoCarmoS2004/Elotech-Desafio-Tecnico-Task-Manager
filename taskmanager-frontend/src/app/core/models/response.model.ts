export interface ApiResponse<T> {
  id: string;
  data: T;
  message: string;
}

export interface PageInfo {
  size: number;
  number: number;
  totalElements: number;
  totalPages: number;
}

export interface PaginatedResponse<T> {
  content: T[];
  page: PageInfo;
}