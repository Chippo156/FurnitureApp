import { Injectable } from '@angular/core';
import { environtment } from '../environments/environment';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class PaymentService {
  private apiVNPay = `${environtment.apiBaseUrl}/payment/vn-pay`;
  private apiVNPayCallback = `${environtment.apiBaseUrl}/payment/vnpay-callback`;

  constructor(private http: HttpClient) {}

  getCreatePayment(
    bankCode: string,
    amount: number,
    orderId: number
  ): Observable<any> {
    const params = new HttpParams()
      .set('bankCode', bankCode)
      .set('orderId', orderId.toString())
      .set('amount', amount.toString());
    return this.http.get(this.apiVNPay, { params });
  }
  getInfoPayment(params: any): Observable<any> {
    return this.http.get(this.apiVNPayCallback, { params });
  }
}
