import {
  Component,
  OnInit,
  QueryList,
  ViewChild,
  ViewChildren,
} from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Product } from '../../models/product';
import { Category } from '../../models/category';
import { Router } from '@angular/router';
import { ProductService } from '../../service/product.service';
import { environtment } from '../../environments/environment';
import { CommmentService } from '../../service/comment.service';
import { OrderService } from '../../service/order.service';
import { SoldProduct } from '../../responses/SoldProduct';
import { ProductImage } from '../../models/product.image';

@Component({
  selector: 'app-product-favorite',
  templateUrl: './product-favorite.component.html',
  styleUrls: ['./product-favorite.component.scss'],
})
export class ProductFavoriteComponent implements OnInit {
  products: Product[] = [];
  productsSort: Product[] = [];
  productsFilter: Product[] = [];
  totalPageFilter: number = 0;
  categories: Category[] = [];
  currentPage: number = 0;
  itemsPerPage: number = 16;
  totalPages: number = 0;
  visiblePages: number[] = [];
  keyword: string = '';
  selectedCategoryId: number = 0;
  soldQuantity: SoldProduct[] = [];
  listRating: Map<number, number> = new Map<number, number>();
  listComment: Map<number, number> = new Map<number, number>();
  hoveredProductId: number | null = null;
  hoveredImage: string | null = null;
  colors: string[] = [];
  checkColor: string = '';
  checkLoad: boolean = false;
  product_favorite: number[] = [];
  constructor(
    private router: Router,
    private productService: ProductService,
    private commentService: CommmentService,
    private orderService: OrderService
  ) {
    this.selectedCategoryId = 0;
    this.keyword = '';
  }

  ngOnInit() {
    debugger;
    window.scrollTo(0, 0);
    // this.getProducts(
    //   this.keyword,
    //   this.selectedCategoryId,
    //   this.currentPage,
    //   this.itemsPerPage
    // );
    this.getCountQuantityProduct();
    this.getColors();

    const productFavoriteData = JSON.parse(
      localStorage.getItem('product_favorite')!
    );
    if (Array.isArray(productFavoriteData)) {
      this.product_favorite = productFavoriteData;
      console.log(this.product_favorite);
    } else {
      this.product_favorite = [];
      console.log(this.product_favorite);
    }
    this.getProductByIds();
  }
  getProductByIds() {
    this.productService.getProductByIds(this.product_favorite).subscribe({
      next: (response: any) => {
        response.productResponses.forEach((product: Product) => {
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
        });
        this.products = response.productResponses;
        console.log(this.products);
      },
      complete: () => {},
      error(err) {
        console.log(err);
      },
    });
  }

  onPageChange(page: number) {
    this.checkLoad = false;
    debugger;
    window.scrollTo(0, 0);
    this.currentPage = page;
    this.getProducts(
      this.keyword,
      this.selectedCategoryId,
      this.currentPage - 1,
      this.itemsPerPage
    );
    const element = document.getElementById('select') as HTMLSelectElement;
    element.value = '0';
  }
  getProducts(
    keyword: string,
    selectedCategoryId: number,
    page: number,
    limit: number
  ) {
    this.productService
      .getProducts(keyword, selectedCategoryId, page, limit)
      .subscribe({
        next: (response: any) => {
          debugger;
          response.productResponses.forEach((product: Product) => {
            let flag = 0;
            product.product_images.forEach((product_images: ProductImage) => {
              if (flag === 1) {
                product.url = `${environtment.apiBaseUrl}/products/viewImages/${product_images.image_url}`;
              } else if (flag === 2) {
                return;
              }
              flag++;
            });

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
          });
          this.products = response.productResponses;
          this.checkLoad = true;
          this.totalPages = response.totalPage;
          this.totalPageFilter = this.totalPages;
          this.productsFilter = this.products;
          this.visiblePages = this.generateVisiblePages(
            this.currentPage,
            this.totalPages
          );
        },
        complete: () => {
          debugger;
          // this.getRating();
          this.getProductsRating();
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
  getProductDetail(productId: number) {
    this.router.navigate(['/products', productId]);
  }
  getProductsRating() {
    this.productService.getProductsRating().subscribe({
      next: (response: any) => {
        debugger;
        response.forEach((data: any) => {
          this.listRating.set(data.product_id, data.rating);
          this.listComment.set(data.product_id, data.evaluation);
        });
      },
      error: (error) => {
        debugger;
        console.log(error);
      },
    });
  }
  getRatingProductId(productId: number): any[] {
    const rating = this.listRating.get(productId) as number;

    if (isNaN(rating) || rating === undefined) {
      return Array(4);
    }
    return Array(Math.round(rating));
  }

  getEvaluationProductId(productId: number): number {
    return this.listComment.get(productId)!;
  }
  getCountQuantityProduct() {
    this.orderService.countQuantityProductInOrder().subscribe({
      next: (response: any) => {
        debugger;
        response.forEach((product: SoldProduct) => {
          this.soldQuantity.push(product);
        });
      },
      error: (error) => {
        debugger;
        console.log(error);
      },
    });
  }
  getSoldQuantity(productId: number): number {
    const product = this.soldQuantity.find(
      (product) => product.productId === productId
    );
    return product ? product.count : 0;
  }
  formatQuantity(quantity: number): string {
    return quantity > 1000
      ? (quantity / 1000).toFixed(1) + 'k'
      : quantity.toString();
  }

  hoverImageToImage(productId: number) {
    this.hoveredProductId = productId;
    this.getImageFromHover(productId);
  }
  hoverOutImage() {
    this.hoveredProductId = null;
    this.hoveredImage = null;
  }

  getImageFromHover(productId: number) {
    const product = this.products.find((product) => product.id === productId);
    if (product) {
      this.hoveredImage = `${environtment.apiBaseUrl}/products/viewImages/${product.thumbnail}`;
    }
  }

  getColors() {
    this.productService.getCodeColors().subscribe({
      next: (response: any) => {
        debugger;
        this.colors = response;
      },
      complete: () => {
        debugger;
      },
      error: (error) => {
        console.log(error);
      },
    });
  }
}
