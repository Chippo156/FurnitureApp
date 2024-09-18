import { Injectable } from '@angular/core';
import { environtment } from '../environments/environment';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class CouponService {
  private apiCouponBase = `${environtment.apiBaseUrl}/coupons`;
  constructor(private http: HttpClient) {}

  getCoupon(): Observable<any> {
    return this.http.get(`${this.apiCouponBase}`);
  }
  getCouponByCode(code: string): Observable<any> {
    return this.http.get(`${this.apiCouponBase}/code/${code}`);
  }
}
