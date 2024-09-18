import { Injectable } from '@angular/core';
import { environtment } from '../environments/environment';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { CategoryDTO } from '../dtos/categoryDto';

@Injectable({
  providedIn: 'root',
})
export class CategoryService {
  private apiCategoryBase = `${environtment.apiBaseUrl}/categories`;

  constructor(private http: HttpClient) {}
  getAllCategories(
    keyword: string,
    page: number,
    limit: number
  ): Observable<any> {
    const params = new HttpParams()
      .set('keyword', keyword)
      .set('page', page.toString())
      .set('limit', limit.toString());
    return this.http.get(this.apiCategoryBase, { params });
  }
  createCategory(categoryDto: CategoryDTO): Observable<any> {
    return this.http.post(this.apiCategoryBase, categoryDto);
  }
  updateCategory(id: number, category_name: CategoryDTO): Observable<any> {
    return this.http.put(`${this.apiCategoryBase}/${id}`, category_name);
  }
  deleteCategory(id: number): Observable<any> {
    return this.http.delete(`${this.apiCategoryBase}/${id}`);
  }
}
