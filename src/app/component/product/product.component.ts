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
import { CategoryService } from 'src/app/service/category.service';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.scss'],
})
export class ProductComponent implements OnInit {
  products: Product[] = [];
  temp: Set<Product> = new Set<Product>();
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
  productColor: Product[] = [];
  checkColor: string = '';
  checkLoad: boolean = false;
  colors: Set<string> = new Set();
  selectedCheckboxes: Set<number> = new Set();

  constructor(
    private router: Router,
    private productService: ProductService,
    private commentService: CommmentService,
    private orderService: OrderService,
    private categoryService: CategoryService
  ) {
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
    this.getCountQuantityProduct();
    // this.getColors();
    this.getAllCategory();
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
            this.colors.add(product.code_color);
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
  onSortChange(event?: Event) {
    debugger;
    const selectElement = event!.target as HTMLSelectElement;
    const selectValue = selectElement.value;

    if (selectValue === '0') {
      this.getProducts(
        this.keyword,
        this.selectedCategoryId,
        this.currentPage,
        this.itemsPerPage
      );
    }
    if (selectValue === '1') {
      this.products = this.products.sort((a, b) =>
        a.name.localeCompare(b.name)
      );
    }
    if (selectValue === '2') {
      this.products = this.products.sort((a, b) => a.price - b.price);
    }
    if (selectValue === '3') {
      this.products = this.products.sort((a, b) => b.price - a.price);
    }
    if (selectValue === '4') {
      debugger;
      this.products = this.products.filter((product) => {
        debugger;
        const today = new Date();
        const productDate = new Date(product.created_at);
        console.log(productDate);

        const diffInTime = today.getTime() - productDate.getTime();
        const diffInDays = diffInTime / (1000 * 3600 * 24);
        console.log(diffInDays);

        return diffInDays <= 10;
      });
    }
    if (selectValue === '5') {
      debugger;
      this.products = this.products.filter((product) => {
        debugger;
        const today = new Date();
        const productDate = new Date(product.created_at);
        console.log(productDate);

        const diffInTime = today.getTime() - productDate.getTime();
        const diffInDays = diffInTime / (1000 * 3600 * 24);
        console.log(diffInDays);

        return diffInDays > 10;
      });
    }
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
  filterProductByCategory() {
    let categoryName = (
      document.querySelector(
        'input[name="category"]:checked'
      ) as HTMLInputElement
    )?.value;
    console.log(categoryName);
    if (categoryName === undefined) {
      this.products = this.productsFilter;
      console.log(this.productsFilter);
    }
    this.productService.getProductByCategoryName(categoryName).subscribe({
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
        if (this.products.length === 0) {
          this.products = this.productsFilter;
          this.totalPages = this.totalPageFilter;
        }
        if (response.totalPage >= 0 && response.productResponses.length > 0) {
          this.totalPages = response.totalPage;
        }

        this.visiblePages = this.generateVisiblePages(
          this.currentPage,
          this.totalPages
        );
      },
      complete: () => {
        debugger;
        this.getProductsRating();
      },
      error: (error) => {
        debugger;
        console.log(error);
      },
    });
  }
  filterProductByPrice() {
    this.products = this.productsFilter;
    let checkboxes = Array.from(
      document.querySelectorAll('input[name="price"]:checked')
    );
    let listPrice = checkboxes.map(
      (checkbox) => (checkbox as HTMLInputElement).value
    );
    let productPrice: Product[] = [];
    console.log(productPrice);
    listPrice.forEach((price) => {
      if (price === '1') {
        this.products.forEach((product) => {
          if (product.price < 500000) {
            productPrice.push(product);
          }
        });
      }
      if (price === '2') {
        this.products.forEach((product) => {
          if (product.price >= 500000 && product.price < 1000000) {
            productPrice.push(product);
          }
        });
      }
      if (price === '3') {
        this.products.forEach((product) => {
          if (product.price >= 1000000 && product.price < 2000000) {
            productPrice.push(product);
          }
        });
      }
      if (price === '4') {
        this.products.forEach((product) => {
          if (product.price >= 2000000 && product.price < 5000000) {
            productPrice.push(product);
          }
        });
      }
      if (price === '5') {
        this.products.forEach((product) => {
          if (product.price >= 5000000) {
            productPrice.push(product);
          }
        });
      }
    });
    this.products = productPrice;
    if (productPrice.length === 0) {
      this.products = this.productsFilter;
    }
  }

  filterProductByColor(color: string) {
    this.products = this.productsFilter;
    let productColor: Product[] = [];
    this.products.forEach((product) => {
      if (product.code_color === color) {
        productColor.push(product);
      }
    });
    this.products = productColor;
    if (productColor.length === 0) {
      this.products = this.productsFilter;
    }
  }
  addFavorite(productId: number) {
    const heart = document.querySelector('.fa-heart');
    heart?.classList.add('addFavorite');
  }
  getAllCategory() {
    this.categoryService.getAllCategories('', 0, 10).subscribe({
      next: (response: any) => {
        debugger;
        this.categories = response.categories;
      },
      complete: () => {},
      error: (error) => {
        debugger;
        console.log(error);
      },
    });
  }
  searchProductByCategory(categoryId: number, categoryName: string) {
    debugger;
    this.selectedCategoryId = categoryId;
    this.getProducts(
      this.keyword,
      this.selectedCategoryId,
      this.currentPage,
      this.itemsPerPage
    );
  }
  onCheckboxChange(event: Event) {
    const checkbox = event.target as HTMLInputElement;
    const value = parseInt(checkbox.value, 10);
    if (checkbox.checked) {
      this.selectedCheckboxes.add(value);
    } else {
      this.selectedCheckboxes.delete(value);
    }
  }
  reset() {
    this.products = this.productsFilter;
    this.selectedCheckboxes.clear();
    this.checkColor = '';
  }
}
