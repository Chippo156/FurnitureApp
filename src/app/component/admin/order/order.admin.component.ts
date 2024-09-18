import { Component, EventEmitter, Output } from '@angular/core';
import { Product } from '../../../models/product';
import { Category } from '../../../models/category';
import { ProductService } from '../../../service/product.service';
import { environtment } from '../../../environments/environment';
import { OrderResponse } from '../../../responses/orderResponse';
import { OrderService } from '../../../service/order.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-order-admin',
  templateUrl: './order.admin.component.html',
  styleUrls: ['./order.admin.component.scss'],
})
export class OrderAdminComponent {
  orders: OrderResponse[] = [];

  currentPage: number = 0;
  itemsPerPage: number = 12;
  totalPages: number = 0;
  visiblePages: number[] = [];
  keyword: string = '';

  constructor(private orderService: OrderService, private router: Router) {
    debugger;
  }
  ngOnInit(): void {
    debugger;
    window.scrollTo(0, 0);
    this.getOrders(this.keyword, this.currentPage, this.itemsPerPage);
  }
  getOrders(keyword: string, page: number, limit: number) {
    this.orderService.getAllOrders(keyword, page, limit).subscribe({
      next: (response: any) => {
        response.orders.forEach((order: OrderResponse) => {
          order.order_date = new Date(order.order_date);
          order.shipping_date = new Date(order.shipping_date);
        });
        this.orders = response.orders;

        this.totalPages = response.totalPage;
        this.visiblePages = this.generateVisiblePages(
          this.currentPage,
          this.totalPages
        );
        debugger;
      },
      complete: () => {
        debugger;
      },
      error: (error) => {
        debugger;
        console.log(error);
      },
    });
  }
  generateVisiblePages(currentPage: number, totalPages: number): number[] {
    const maxVisiblePage = 5;
    const haftVisiblePage = Math.floor(maxVisiblePage / 2);
    let startPage = Math.max(currentPage - haftVisiblePage, 1);
    let endPage = Math.min(startPage + maxVisiblePage - 1, totalPages);
    if (endPage - startPage + 1 < maxVisiblePage) {
      startPage = Math.max(endPage - maxVisiblePage + 1, 1);
    }
    return new Array(endPage - startPage + 1)
      .fill(0)
      .map((_, index) => startPage + index);
  }
  onPageChange(page: number) {
    debugger;
    this.currentPage = page < 0 ? 0 : page;
    localStorage.setItem('currentOrderAdminPage', String(this.currentPage));
    this.getOrders(this.keyword, this.currentPage, this.itemsPerPage);
  }
  getOrderDetails(orderId: number) {
    this.router.navigate(['/admin/order-details', orderId]);
  }

  viewDetails(order: OrderResponse) {
    debugger;
    this.router.navigate(['/admin/orders', order.id]);
  }
  onRemove(orderId: number) {
    this.orderService.deleteOrder(orderId).subscribe({
      next: (response: any) => {
        alert(response.message);
        debugger;
      },
      complete: () => {
        this.getOrders(this.keyword, this.currentPage, this.itemsPerPage);
        debugger;
      },
      error: (error) => {
        debugger;
        console.log(error);
      },
    });
  }
}
