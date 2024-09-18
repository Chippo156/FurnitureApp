import {
  AfterViewChecked,
  AfterViewInit,
  Component,
  Injectable,
  OnInit,
  Renderer2,
} from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import {
  ActivatedRoute,
  NavigationExtras,
  Route,
  Router,
} from '@angular/router';
import { ProductService } from '../../service/product.service';
import { Product } from '../../models/product';
import { Category } from '../../models/category';
import { environtment } from '../../environments/environment';
import { PaymentService } from '../../service/payment.service';
import { CommmentService } from '../../service/comment.service';
import { Comment } from '../../models/comment';
import { OrderService } from '../../service/order.service';
import { SoldProduct } from '../../responses/SoldProduct';
import { ProductImage } from '../../models/product.image';
import { CategoryService } from 'src/app/service/category.service';
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
@Injectable()
export class HomeComponent implements OnInit, AfterViewInit, AfterViewChecked {
  products: Product[] = [];
  temp: Product[] = [];
  productImage!: Product;
  productShowMore: Product[] = [];
  categories: Category[] = [];
  currentPage: number = 0;
  itemsPerPage: number = 12;
  pages: number[] = [];
  totalPages: number = 0;
  visiblePages: number[] = [];
  keyword: string = '';
  selectedCategoryId: number = 0;
  checkShowMore: boolean = false;
  productId: number = 0;
  checkImage: boolean = false;
  soldQuantity: SoldProduct[] = [];
  comments: number = 0;
  listRating: Map<number, number> = new Map<number, number>();
  listComment: Map<number, number> = new Map<number, number>();
  checkLoad: boolean = false;
  product_favorite: Set<number> = new Set<number>();
  viewChecked: boolean = false;
  valueCategoryCheck: string = 'All categories';

  constructor(
    private router: Router,
    private productService: ProductService,
    private paymentService: PaymentService,
    private route: ActivatedRoute,
    private commentService: CommmentService,
    private orderService: OrderService,
    private renderer: Renderer2,
    private categoryService: CategoryService
  ) {
    this.keyword = '';
  }
  ngAfterViewChecked(): void {
    if (!this.viewChecked) {
      this.viewFavorite();
      this.viewChecked = true;
    }
  }
  ngAfterViewInit(): void {
    debugger;
    const productFavoriteData = JSON.parse(
      localStorage.getItem('product_favorite')!
    );
    if (Array.isArray(productFavoriteData)) {
      this.product_favorite = new Set<number>(productFavoriteData);
      console.log(this.product_favorite);
    } else {
      this.product_favorite = new Set<number>();
      console.log(this.product_favorite);
    }
  }

  ngOnInit(): void {
    debugger;
    window.scrollTo(0, 0);
    this.getProducts(
      this.keyword,
      this.selectedCategoryId,
      this.currentPage,
      this.itemsPerPage
    );
    this.getAllCategory();
    this.route.queryParams.subscribe((params) => {
      const paymentStatus = params['paymentStatus'];
      if (paymentStatus === '00') {
        alert('Thanh toán thành công');
        this.clearPaymentStatus();
      } else if (paymentStatus === '01') {
        alert('Thanh toán thất bại');
        this.clearPaymentStatus();
      }
    });
    this.getCountQuantityProduct();
  }
  clearPaymentStatus() {
    const navigationExtras: NavigationExtras = {
      replaceUrl: true, // Replace the current URL in the history
    };
    this.router.navigate(['/'], navigationExtras);
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
          this.temp = response.productResponses;
          this.checkLoad = true;
          this.totalPages = response.totalPages;
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
  checkShowMoreProducts() {
    this.checkLoad = false;
    ++this.currentPage;
    this.getProducts(
      this.keyword,
      this.selectedCategoryId,
      this.currentPage,
      this.itemsPerPage
    );
    this.productShowMore = this.products;
    this.checkShowMore = true;
  }
  onProductClick(productId: number) {
    this.router.navigate(['/products', productId]);
  }
  searchProduct() {
    this.getProducts(
      this.keyword,
      this.selectedCategoryId,
      this.currentPage,
      this.itemsPerPage
    );
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
  hoveredProductId: number | null = null;
  hoveredImage: string | null = null;

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
    const productShowMore = this.productShowMore.find(
      (product) => product.id === productId
    );
    if (product) {
      this.hoveredImage = `${environtment.apiBaseUrl}/products/viewImages/${product.thumbnail}`;
    }
    if (productShowMore) {
      this.hoveredImage = `${environtment.apiBaseUrl}/products/viewImages/${productShowMore.thumbnail}`;
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
  checkLoader(): boolean {
    if (this.products.length > 0) {
      return true;
    }
    return false;
  }
  checkShowMoreLoader(): boolean {
    if (this.productShowMore.length > 0) {
      return true;
    }
    if (this.products.length > 0) {
      return true;
    } else {
      return false;
    }
  }
  addFavorite(productId: number) {
    this.product_favorite.add(productId);
    localStorage.setItem(
      'product_favorite',
      JSON.stringify(Array.from(this.product_favorite))
    );
    const heartElement = document.getElementById('heart' + productId);
    if (heartElement) {
      heartElement.setAttribute('style', 'color: red');
    } else {
      console.log('Element not found');
    }
  }
  addAndRemoveFavorite(id: number) {
    let flag = false;
    this.product_favorite.forEach((productId) => {
      const heartElement = this.renderer.selectRootElement(
        '#heart' + productId,
        true
      );
      if (heartElement && id === productId) {
        flag = true;
        this.renderer.setStyle(heartElement, 'color', '#cabffd');
        this.product_favorite.delete(productId);
        localStorage.setItem(
          'product_favorite',
          JSON.stringify(Array.from(this.product_favorite))
        );
        return;
      } else {
        console.log('Elememt not found');
      }
    });
    if (flag == false) {
      this.addFavorite(id);
    }
  }
  viewFavorite() {
    this.product_favorite.forEach((productId) => {
      const heartElement = this.renderer.selectRootElement(
        '#heart' + productId,
        true
      );
      if (heartElement) {
        this.renderer.setStyle(heartElement, 'color', 'red');
      } else {
        console.log('Element not found');
      }
    });
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
    this.valueCategoryCheck = categoryName;
    this.selectedCategoryId = categoryId;
    this.getProducts(
      this.keyword,
      this.selectedCategoryId,
      this.currentPage,
      this.itemsPerPage
    );
  }
}
