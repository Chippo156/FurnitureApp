import { Injectable } from '@angular/core';
import { environtment } from '../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, catchError, map } from 'rxjs';
import { CommentDTO } from '../dtos/commentDto';

@Injectable({
  providedIn: 'root',
})
export class CommmentService {
  private apiBaseComment = `${environtment.apiBaseUrl}/comments`;

  constructor(private http: HttpClient) {}

  createComment(commentDto: CommentDTO): Observable<any> {
    return this.http.post(`${this.apiBaseComment}`, commentDto);
  }
  uploadImageCommment(commentId: number, image: File[]): Observable<any> {
    const formData = new FormData();
    image.forEach((file) => {
      formData.append('files', file);
    });
    return this.http.post(
      `${this.apiBaseComment}/uploadImages/${commentId}`,
      formData,
      { responseType: 'text' }
    );
  }
}
