import { Component, OnInit } from '@angular/core';
import { environtment } from 'src/app/environments/environment';
import { OrderDetail } from 'src/app/models/orderDetail';
import { Product } from 'src/app/models/product';
import { ProductImage } from 'src/app/models/product.image';
import { OrderResponse } from 'src/app/responses/orderResponse';
import { OrderService } from 'src/app/service/order.service';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss'],
})
export class OrderComponent implements OnInit {
  orders: OrderResponse[] = [];
  orderDetails: OrderDetail[] = [];
  products: Product[] = [];
  orderTemp: OrderResponse[] = [];
  orderId: number = 0;
  dateFrom: string = '';
  dateTo: string = '';
  userId = this.userService.getUserResponseFromLocalStorage()!.id;
  ngOnInit(): void {
    debugger;
    this.getOrders();
    console.log(this.orders);
  }

  constructor(
    private orderService: OrderService,
    private userService: UserService
  ) {}

  getOrders() {
    debugger;
    const userId = this.userService.getUserResponseFromLocalStorage()!.id;
    this.orderService.getOrdersByUserId(userId).subscribe({
      next: (response: OrderResponse[]) => {
        response.forEach((order) => {
          order.order_date = new Date(order.order_date);
          order.shipping_date = new Date(order.shipping_date);
        });
        this.orders = response;
        this.orderTemp = response;
      },
      error: (error) => {
        console.error(error);
      },
    });
  }
  getProductByOrderId(orderId: number) {
    this.orderService.getProductByOrderId(orderId).subscribe({
      next: (response: Product[]) => {
        response.forEach((product) => {
          product.product_images.forEach((product_images: ProductImage) => {
            product.url = `${environtment.apiBaseUrl}/products/viewImages/${product_images.image_url}`;
          });
        });
        this.products = response;
      },
      error: (error) => {
        console.error(error);
      },
    });
  }
  getOrderDetail(orderId: number) {
    this.orderId = orderId;
    this.orderService.getOrderDetail(orderId).subscribe({
      next: (response: OrderDetail[]) => {
        response.forEach((orderDetail) => {
          orderDetail.product.product_images.forEach(
            (product_images: ProductImage) => {
              orderDetail.product.url = `${environtment.apiBaseUrl}/products/viewImages/${product_images.image_url}`;
            }
          );
        });
        this.orderDetails = response;
      },
      error: (error) => {
        console.error(error);
      },
    });
  }
  getImageUrl(imageUrl: string) {
    return `${environtment.apiBaseUrl}/products/viewImages/${imageUrl}`;
  }
  deleteOrder(id: number) {
    const isConfirm = window.confirm(
      'Are you sure you want to delete this order?'
    );
    if (isConfirm) {
      this.orderService.deleteOrder(id).subscribe({
        next: () => {
          alert('Delete order successfully!');
          this.getOrders();
        },
        error: (error) => {
          console.error(error);
        },
      });
    }
  }
  getOrderWithStatus(status: string) {
    this.orders = [];
    if (status === 'summary') {
      const completedStatus = document.getElementById('summary');
      const li = document.querySelectorAll('li');
      completedStatus!.classList.add('text-indigo-600');
      li.forEach((element) => {
        if (element.id !== 'summary') {
          element.classList.remove('text-indigo-600');
        }
      });
      this.orderTemp.forEach((order) => {
        if (
          order.status === 'pending' ||
          order.status === 'shipped' ||
          order.status === 'processing'
        ) {
          this.orders.push(order);
        }
      });
    } else if (status === 'all') {
      const completedStatus = document.getElementById('all');
      const li = document.querySelectorAll('li');
      completedStatus!.classList.add('text-indigo-600');
      li.forEach((element) => {
        if (element.id !== 'all') {
          element.classList.remove('text-indigo-600');
        }
      });
      this.orders = this.orderTemp;
    } else if (status === 'completed') {
      const completedStatus = document.getElementById('completed');
      const li = document.querySelectorAll('li');
      completedStatus!.classList.add('text-indigo-600');
      li.forEach((element) => {
        if (element.id !== 'completed') {
          element.classList.remove('text-indigo-600');
        }
      });
      this.orderTemp.forEach((order) => {
        if (order.status === 'delivered') {
          this.orders.push(order);
        }
      });
    } else {
      debugger;
      const completedStatus = document.getElementById('cancelled');
      const li = document.querySelectorAll('li');
      completedStatus!.classList.add('text-indigo-600');
      li.forEach((element) => {
        if (element.id !== 'cancelled') {
          element.classList.remove('text-indigo-600');
        }
      });
      this.getOrderCancelled();
    }
    this.filterOrder();
  }
  filterOrder() {
    const dateFrom = new Date(this.dateFrom);
    const dateTo = new Date(this.dateTo);

    if (this.dateFrom === '' || this.dateTo === '') {
      return;
    }
    this.orders = this.orders.filter((order) => {
      const orderDate = new Date(order.order_date);
      return orderDate >= dateFrom && orderDate <= dateTo;
    });
  }
  getOrderCancelled() {
    this.orderService.getOrderCancceled(this.userId).subscribe({
      next: (response: OrderResponse[]) => {
        response.forEach((order) => {
          order.order_date = new Date(order.order_date);
          order.shipping_date = new Date(order.shipping_date);
        });
        this.orders = response;
      },
      error: (error) => {
        console.error(error);
      },
    });
  }
}
