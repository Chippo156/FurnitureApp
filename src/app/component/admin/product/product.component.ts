import { Component, OnInit } from '@angular/core';
import { Product } from '../../../models/product';
import { Router } from '@angular/router';
import { ProductService } from '../../../service/product.service';
import { environtment } from '../../../environments/environment';
import { timestamp } from 'rxjs';

@Component({
  selector: 'app-admin-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.scss'],
})
export class ProductAdminComponent implements OnInit {
  products: Product[] = [];
  currentPage: number = 0;
  itemsPerPage: number = 15;
  totalPages: number = 0;
  visiblePages: number[] = [];
  keyword: string = '';
  selectedCategoryId: number = 0;

  productName: string = '';
  price: number = 0;
  description: string = '';
  size: string = '';
  color: string = '';

  constructor(private router: Router, private productService: ProductService) {
    this.selectedCategoryId = 0;
    this.keyword = '';
  }

  ngOnInit() {
    debugger;
    window.scrollTo(0, 0);
    this.getProducts(
      this.keyword,
      this.selectedCategoryId,
      this.currentPage,
      this.itemsPerPage
    );
  }
  onPageChange(page: number) {
    debugger;
    window.scrollTo(0, 0);
    this.currentPage = page;
    this.getProducts(
      this.keyword,
      this.selectedCategoryId,
      this.currentPage - 1,
      this.itemsPerPage
    );
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
            product.created_at = new Date(product.created_at);

            let formattedNumber = product.price.toLocaleString('vi-VN');
            product.price = parseFloat(formattedNumber);
          });
          this.products = response.productResponses;
          this.totalPages = response.totalPage;
          this.visiblePages = this.generateVisiblePages(
            this.currentPage,
            this.totalPages
          );
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
  viewDetails(product: Product) {
    debugger;
    this.router.navigate(['/admin/products', product.id]);
  }
  searchProduct() {
    this.getProducts(
      this.keyword,
      this.selectedCategoryId,
      this.currentPage,
      this.itemsPerPage
    );
  }
  addProduct() {}
  deleteProduct(productId: number) {}
}
