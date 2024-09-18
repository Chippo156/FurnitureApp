import { Injectable } from '@angular/core';
import { ProductService } from './product.service';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CartService {
  private cart: Map<number, number> = new Map<number, number>();
  private cartSize: Map<number, string[]> = new Map<number, string[]>();

  private cartSize1: Map<number, Map<string, number>> = new Map<
    number,
    Map<string, number>
  >();
  constructor(private productService: ProductService) {
    const storeCart = localStorage.getItem('cart');
    const storeCartSize = localStorage.getItem('cartSize') || '';

    if (storeCart) {
      this.cart = new Map(JSON.parse(storeCart));
    }
    if (storeCartSize) {
      this.cartSize = new Map(JSON.parse(storeCartSize));
    }
  }
  addToCart(productId: number, quantity: number = 1, selectedSize: string) {
    {
      debugger;
      if (this.cart.has(productId)) {
        this.cart.set(productId, this.cart.get(productId)! + quantity);
        let string = this.cartSize.get(productId);
        string?.push(selectedSize);
        this.cartSize.set(productId, string!);
      } else {
        this.cart.set(productId, quantity);
        let string = [selectedSize];
        this.cartSize.set(productId, string);
      }

      debugger;
      if (this.cartSize1.has(productId)) {
        let obj = this.cartSize1.get(productId);
        if (!(obj instanceof Map)) {
          // Convert obj to a Map if it's an Object
          if (obj! instanceof Object) {
            obj = new Map(Object.entries(obj));
          } else {
            console.error('Expected a Map or Object, but got:', obj);
            return;
          }
        }
        if (obj.has(selectedSize)) {
          obj.set(selectedSize, obj.get(selectedSize)! + quantity);
        } else {
          obj.set(selectedSize, quantity);
        }
        this.cartSize1.set(productId, obj);
      } else {
        let obj = new Map<string, number>();
        obj.set(selectedSize, quantity);
        this.cartSize1.set(productId, obj);
      }
      this.saveCartToLocalStorage();
    }
  }

  getCart(): Map<number, number> {
    return this.cart;
  }
  getCartSize(): Map<number, string[]> {
    return this.cartSize;
  }
  getCartSize1(): Map<number, Map<string, number>> {
    const cartSize1Data = localStorage.getItem('cartSize1');
    if (cartSize1Data) {
      return new Map(
        JSON.parse(cartSize1Data).map(
          ([k, v]: [number, Map<string, number>]) => [k, new Map(v)]
        )
      );
    } else {
      return new Map();
    }
  }
  private saveCartToLocalStorage() {
    debugger;
    localStorage.setItem(
      'cart',
      JSON.stringify(Array.from(this.cart.entries()))
    );
    localStorage.setItem(
      'cartSize',
      JSON.stringify(Array.from(this.cartSize.entries()))
    );
    localStorage.setItem(
      'cartSize1',
      JSON.stringify(Array.from(this.cartSize1, ([k, v]) => [k, Array.from(v)]))
    );
  }
  clearCart() {
    this.cart.clear();
    this.cartSize.clear();
    this.cartSize1.clear();
    this.saveCartToLocalStorage();
  }
  updateQuantity(productId: number, quantity: number, size: string) {
    const cartSize1Data = this.getCartSize1();
    const cartSize = cartSize1Data.get(productId);
    if (cartSize) {
      cartSize.set(size, quantity);
    }
    this.cartSize1.set(productId, cartSize!);
    this.saveCartToLocalStorage();
  }
  removeProduct(productId: number, size: string) {
    debugger;
    const cartSize1Data = this.getCartSize1();
    const cartSize = cartSize1Data.get(productId);
    if (cartSize) {
      cartSize.delete(size);
      if (cartSize.size === 0) {
        this.cart.delete(productId);
        this.cartSize.delete(productId);
        this.cartSize1.delete(productId);
      } else {
        this.cartSize1.set(productId, cartSize);
      }
    }
    this.saveCartToLocalStorage();
  }
}
