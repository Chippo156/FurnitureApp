import { Injectable } from '@angular/core';
import { environtment } from '../environments/environment';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { OrderDTO } from '../dtos/orderDto';
import { UserService } from './user.service';
import { OrderResponse } from '../responses/orderResponse';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  private apiOrderBase = `${environtment.apiBaseUrl}/orders`;
  private apiOrderDetailBase = `${environtment.apiBaseUrl}/order-details`;

  constructor(private http: HttpClient, private userService: UserService) {}

  placeOrder(orderDTO: OrderDTO): Observable<any> {
    return this.http.post(this.apiOrderBase, orderDTO, {});
  }
  getAllOrders(keyword: string, page: number, limit: number): Observable<any> {
    const params = new HttpParams()
      .set('keyword', keyword)
      .set('page', page.toString())
      .set('limit', limit.toString());

    return this.http.get(this.apiOrderBase, { params });
  }
  getOrderDetail(orderId: number): Observable<any> {
    return this.http.get(`${this.apiOrderDetailBase}/order/${orderId}`);
  }
  deleteOrder(id: number): Observable<any> {
    return this.http.delete(`${this.apiOrderBase}/${id}`);
  }
  deleteOrderDetail(id: number): Observable<any> {
    return this.http.delete(`${this.apiOrderDetailBase}/${id}`);
  }
  countQuantityProductInOrder(): Observable<any> {
    return this.http.get(`${this.apiOrderDetailBase}/countQuantity`);
  }
  getOrdersByUserId(userId: number): Observable<any> {
    return this.http.get(`${this.apiOrderBase}/user/${userId}`);
  }
  getOrderById(id: number): Observable<any> {
    return this.http.get(`${this.apiOrderBase}/${id}`);
  }
  updateOrder(id: number, order: OrderResponse): Observable<any> {
    const orderDTO: OrderDTO = {
      user_id: order.user_id,
      full_name: order.full_name,
      phone_number: order.phone_number,
      email: order.email,
      address: order.address,
      note: order.note,
      order_status: order.status,
      total_money: order.total_money,
      shipping_method: order.shipping_method,
      payment_method: order.payment_method,
      shipping_address: order.shipping_address,
      tracking_number: order.tracking_number,
      cart_items: order.order_details.map((item) => {
        return {
          product_id: item.product.id,
          quantity: item.number_of_products,
        };
      }),
    };
    return this.http.put(`${this.apiOrderBase}/${id}`, orderDTO);
  }
  getProductByOrderId(orderId: number): Observable<any> {
    return this.http.get(`${this.apiOrderBase}/products/${orderId}`);
  }
  getOrderCancceled(userId: number): Observable<any> {
    return this.http.get(`${this.apiOrderBase}/cancelled/${userId}`);
  }
}
