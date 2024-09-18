import { Component, OnInit } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Product } from '../../models/product';
import { ProductService } from '../../service/product.service';
import { CartService } from '../../service/cart.service';
import { environtment } from '../../environments/environment';
import { Size } from '../../models/sizes';
import { Router } from '@angular/router';
import { AlertService } from 'src/app/service/alert.service';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss'],
})
export class CartComponent implements OnInit {
  cartItems: { product: Product; quantity: number; size: string }[] = [];
  couponCode: string = '';
  totalMoney: number = 0;
  subTotal: number = 0;
  size!: Size;
  selectedSize: string[] = [];
  checkLoad: boolean = false;
  constructor(
    private productService: ProductService,
    private cartService: CartService,
    private router: Router,
    private alert: AlertService
  ) {}
  ngOnInit(): void {
    debugger;
    window.scrollTo(0, 0);

    const cartSize1 = this.cartService.getCartSize1();
    const productIds = Array.from(cartSize1.keys());
    if (productIds.length !== 0) {
      this.productService.getProductByIds(productIds).subscribe({
        next: (products: any) => {
          debugger;
          this.cartItems = [];

          productIds.forEach((productId) => {
            const product = products.productResponses.find(
              (p: Product) => p.id === productId
            );
            if (product) {
              product.url = `${environtment.apiBaseUrl}/products/viewImages/${product.thumbnail}`;
              if (product.product_sale === null) {
                product.product_sale = {
                  id: 0,
                  description: '',
                  sale: 0,
                  newProduct: true,
                  startDate: new Date(),
                  endDate: new Date(),
                };
              }

              let selectedSize = cartSize1.get(productId);
              //cartSize1: Map<number, Map<string, number>>
              if (selectedSize) {
                selectedSize.forEach((value, key) => {
                  const productCopy = { ...product }; // copy the product
                  this.size = product.product_sizes.find(
                    (s: Size) => s.size === key
                  );
                  productCopy.price =
                    ((product.price * (100 - product.product_sale.sale)) /
                      100) *
                    (1 + this.size.priceSize / 100);
                  this.cartItems.push({
                    product: productCopy,
                    quantity: value,
                    size: key,
                  });
                });
              }
            }
          });
        },
        complete: () => {
          debugger;
          this.checkLoad = true;
          this.calculateTotalMoney();
        },
        error: (error) => {
          debugger;
          console.log(error);
        },
      });
    } else {
      this.checkLoad = true;
    }
  }
  calculateTotalMoney() {
    this.totalMoney = this.cartItems.reduce(
      (total, item) => total + item.product.price * item.quantity,
      0
    );
  }
  removeItem(productId: number, size: string) {
    this.cartItems = this.cartItems.filter((item) => item.size !== size);
    this.cartService.removeProduct(productId, size);
    this.calculateTotalMoney();
  }
  onChangeQuantity(productId: number, quantity: number, size: string) {
    if (quantity < 1) {
      quantity = 0;
      return;
    }
    this.cartService.updateQuantity(productId, quantity, size);
    this.cartItems = this.cartItems.map((item) => {
      if (item.product.id === productId && item.size === size) {
        item.quantity = quantity;
      }
      return item;
    });
    this.calculateTotalMoney();
  }
  checkCartItem() {
    if (this.cartItems.length == 0) {
      this.alert.showAlert('error', 'Cart is empty, please add product');
      return false;
    }
    this.router.navigate(['/cartDetail']);
    return true;
  }
}
