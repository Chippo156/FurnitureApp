import { Component, OnInit } from '@angular/core';
import { Product } from '../../../../models/product';
import { Coupon } from '../../../../models/coupon';
import { OrderResponse } from 'src/app/responses/orderResponse';
import { CartService } from 'src/app/service/cart.service';
import { ProductService } from 'src/app/service/product.service';
import { CouponService } from 'src/app/service/coupon.service';
import { OrderService } from 'src/app/service/order.service';
import { ActivatedRoute, Router } from '@angular/router';
import { OrderDetail } from 'src/app/models/orderDetail';
import { environtment } from 'src/app/environments/environment';

@Component({
  selector: 'app-detail-order-admin',
  templateUrl: './orderDetails.admin.component.html',
  styleUrls: ['./orderDetails.admin.component.scss'],
})
export class OrderDetailsAdminComponent implements OnInit {
  cartItems: { product: Product; quantity: number }[] = [];
  couponCode: string = '';
  totalAmount: number = 0;
  discountValue: number = 0;
  orderId: number = 0;
  total: number = 0;
  coupons: Coupon[] = [];
  orderResponse: OrderResponse = {
    id: 0,
    user_id: 0,
    full_name: '',
    phone_number: '',
    email: '',
    address: '',
    note: '',
    order_date: new Date(),
    status: '',
    total_money: 0,
    shipping_method: '',
    shipping_address: '',
    shipping_date: new Date(),
    payment_method: '',
    order_details: [],
    is_active: true,
    tracking_number: '',
  };
  constructor(
    private cartService: CartService,
    private productService: ProductService,
    private couponService: CouponService,
    private orderService: OrderService,
    private router: ActivatedRoute,
    private route: Router
  ) {}

  ngOnInit(): void {
    debugger;
    this.getOrderDetails();
  }
  getOrderDetails() {
    debugger;
    this.orderId = Number(this.router.snapshot.paramMap.get('id'));
    this.orderService.getOrderById(this.orderId).subscribe({
      next: (response: any) => {
        this.orderResponse.id = response.id;
        this.orderResponse.user_id = response.user_id;
        this.orderResponse.full_name = response.full_name;
        this.orderResponse.email = response.email;
        this.orderResponse.phone_number = response.phone_number;
        this.orderResponse.address = response.address;
        this.orderResponse.note = response.note;
        this.orderResponse.is_active = response.is_active;
        debugger;
        const day = this.orderResponse.order_date.getDay();
        const month = this.orderResponse.order_date.getMonth() + 1;
        const year = this.orderResponse.order_date.getFullYear();
        const date = new Date(this.orderResponse.order_date);
        this.orderResponse.order_date = date;
        debugger;
        this.orderResponse.order_details = response.order_details.map(
          (order_detail: OrderDetail) => {
            order_detail.product.thumbnail = `${environtment.apiBaseUrl}/products/viewImages/${order_detail.product.thumbnail}`;
            return order_detail;
          }
        );
        this.orderResponse.payment_method = response.payment_method;
        this.orderResponse.shipping_date = new Date(
          response.shipping_date[0],
          response.shipping_date[1] - 1,
          response.shipping_date[2]
        );
        this.orderResponse.shipping_method = response.shipping_method;
        this.orderResponse.status = response.status;
        this.orderResponse.shipping_address = response.shipping_address;
        this.orderResponse.total_money = response.total_money;
        this.totalAmount = this.orderResponse.total_money;
        this.calculateTotal();
      },
      complete: () => {
        debugger;
      },
      error: (error: any) => {
        debugger;
        console.log('error fetching data: ', error.getMessage());
      },
    });
  }
  calculateTotal() {
    this.orderResponse.order_details.forEach((item) => {
      this.total += item.number_of_products * item.product.price;
    });
  }
  
  updateOrder(orderResponse: OrderResponse) {
    if (!orderResponse.is_active) {
      alert('You can not update this order because this order removed');
      this.route.navigate(['../'], { relativeTo: this.router });
      return;
    }
    debugger;
    this.orderService
      .updateOrder(orderResponse.id, this.orderResponse)
      .subscribe({
        next: (response: any) => {
          debugger;
          alert(`Order updated successfully`);
          this.route.navigate(['../'], { relativeTo: this.router });
        },
        complete: () => {},
        error: (error: any) => {
          console.log('Error fetching data: ' + error.error.message);
        },
      });
  }
}
