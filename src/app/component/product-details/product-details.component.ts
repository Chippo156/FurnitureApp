import {
  AfterViewInit,
  Component,
  ElementRef,
  EventEmitter,
  Input,
  OnInit,
  Output,
  ViewChild,
} from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { BrowserModule } from '@angular/platform-browser';
import { ProductService } from '../../service/product.service';
import { Product } from '../../models/product';
import { ProductImage } from '../../models/product.image';
import { environtment } from '../../environments/environment';
import { Category } from '../../models/category';
import { CartService } from '../../service/cart.service';
import { ActivatedRoute, Route, Router } from '@angular/router';
import { SoldProduct } from '../../responses/SoldProduct';
import { OrderService } from '../../service/order.service';
import { CommmentService } from '../../service/comment.service';
import { Color } from '../../models/colors';
import { style } from '@angular/animations';
import { CommentImage } from '../../models/comment.image';
import { Comment } from '../../models/comment';
import { Size } from '../../models/sizes';
import { CommentDTO } from 'src/app/dtos/commentDto';
import { UserService } from 'src/app/service/user.service';
import * as THREE from 'three';

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.scss'],
})
export class ProductDetailsComponent implements OnInit {
  constructor(
    private productService: ProductService,
    private cartService: CartService,
    private router: ActivatedRoute,
    private route: Router,
    private orderService: OrderService,
    private commentService: CommmentService,
    private userService: UserService
  ) {}
  @ViewChild('container') containerRef!: ElementRef;
  productCheckColor: Product[] = [];
  checkShowMore: boolean = false;
  product!: Product;
  productId: number = 1;
  currentImageIndex: number = 0;
  quantity: number = 1;
  quantityChange = new EventEmitter<number>();
  products: Product[] = [];
  categories: Category[] = [];
  currentPage: number = 0;
  itemsPerPage: number = 8;
  totalPages: number = 0;
  visiblePages: number[] = [];
  keyword: string = '';
  selectedCategoryId: number = 0;
  soldQuantity: SoldProduct[] = [];
  listRating: Map<number, number> = new Map<number, number>();
  priceSize: number = 0;
  selectedSize: string = '';
  selectedColor: string = '';
  colors: string[] = [];
  checkInfor: string = 'review';
  sizes: Size[] = [];
  comments: Comment[] = [];
  checkLoad: boolean = false;
  commentContent: string = '';
  commentRating: number = 0;
  commentImages: File[] = [];
  user_id: number | undefined;

  @Output() imageUrl = new EventEmitter<string>();
  ngOnInit() {
    debugger;
    this.user_id = this.userService.getUserResponseFromLocalStorage()?.id;
    window.scrollTo(0, 0);
    this.getProductDetails(0);
    console.log(this.product);

    this.getCountQuantityProduct();
  }
  getProductDetails(id: number) {
    debugger;
    const idParam = this.router.snapshot.paramMap.get('id');
    if (id !== 0) {
      this.productId = id;
    }
    if (idParam !== null && id === 0) {
      this.productId = parseInt(idParam!);
    }
    if (!isNaN(this.productId)) {
      this.productService.getProductDetails(this.productId).subscribe({
        next: (response: any) => {
          debugger;
          if (response.product_images && response.product_images.length > 0) {
            response.product_images.forEach((product_images: ProductImage) => {
              product_images.image_url = `${environtment.apiBaseUrl}/products/viewImages/${product_images.image_url}`;
            });
          }
          if (response.product_sale === null) {
            response.product_sale = {
              id: 0,
              description: '',
              sale: 0,
              newProduct: true,
              startDate: new Date(),
              endDate: new Date(),
            };
          }
          debugger;
          this.product = response;
          this.showImage(0);
        },
        complete: () => {
          debugger;
          this.getProductDetailSizes();
          // this.clickSize(this.selectedSize);
          this.getProductByCategory(this.product.classify_color_id);
          this.getCommentByProductId(this.product.id);
          this.checkLoad = true;
        },
        error: (error) => {
          debugger;
          console.log(error);
        },
      });
    } else {
      console.log('Product ID is not a number', idParam);
    }
  }
  showImage(index: number) {
    debugger;
    if (
      this.product &&
      this.product.product_images &&
      this.product.product_images.length > 0
    ) {
      if (index < 0) {
        this.currentImageIndex = 0;
      } else if (index >= this.product.product_images.length) {
        this.currentImageIndex = this.product.product_images.length - 1;
      } else {
        this.currentImageIndex = index;
      }
    }
  }
  nextImage() {
    debugger;
    this.showImage(this.currentImageIndex + 1);
  }
  priviousImage() {
    debugger;
    this.showImage(this.currentImageIndex - 1);
  }
  thumbnailClick(index: number) {
    debugger;
    this.showImage(index);
  }
  increaseQuantity() {
    debugger;
    this.quantity++;
  }
  decreaseQuantity() {
    debugger;
    if (this.quantity > 1) {
      this.quantity--;
    }
  }
  addToCart() {
    debugger;
    if (this.product) {
      this.cartService.addToCart(
        this.productId,
        this.quantity,
        this.selectedSize
      );
    } else {
      console.log('Cannot add cart because product is not available');
    }
  }

  getProductDetail(productId: number) {
    this.route.navigate(['/products', productId]);
    setTimeout(() => {
      this.getProductDetails(0);
      window.scrollTo(0, 0);
    }, 10);
  }

  reviewRating(rating: number) {
    return Array(rating);
  }
  getRatingProductId(productId: number): any[] {
    const rating = this.listRating.get(productId) as number;

    if (isNaN(rating) || rating === undefined) {
      return Array(4);
    }
    return Array(Math.round(rating));
  }
  getCountQuantityProduct() {
    this.orderService.countQuantityProductInOrder().subscribe({
      next: (response: any) => {
        debugger;
        response.forEach((product: SoldProduct) => {
          this.soldQuantity.push(product);
        });
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
  clickSize(size: string) {
    debugger;
    console.log(size);
    this.selectedSize = size;
    this.priceSize = this.sizes.find((s) => s.size === size)!.priceSize;
  }
  clickColor(color: string) {
    debugger;
    if (this.selectedColor !== color) {
      this.productCheckColor.find((product) => product.code_color === color)!;
      this.colors = [];
      this.getProductDetails(
        this.productCheckColor.find((product) => product.code_color === color)!
          .id
      );
      this.selectedColor = this.product.code_color;
    }
  }
  getProductByCategory(categoryId: number) {
    this.productService.getProductByClassifyColorId(categoryId).subscribe({
      next: (response: any) => {
        this.productCheckColor = response.productResponses;
        debugger;
      },
      complete: () => {
        this.getColorsProductDetails();
      },
      error: (error) => {
        debugger;
        console.log(error);
      },
    });
  }

  getColorsProductDetails() {
    debugger;
    this.productCheckColor.forEach((product: Product) => {
      if (product) {
        this.colors.push(product.code_color);
      }
    });
    this.selectedColor = this.product.code_color;
  }
  getProductDetailSizes() {
    debugger;
    if (this.product) {
      this.productService.getProductDetailSizes(this.product.id).subscribe({
        next: (response: any) => {
          debugger;
          this.sizes = response;
        },
        complete: () => {
          this.clickSize(this.sizes[0].size);

          debugger;
        },
        error: (error) => {
          debugger;
          console.log(error);
        },
      });
    }
  }
  checkInforProduct(infor: string) {
    const element1 = document.getElementById('des');
    const element2 = document.getElementById('review')!;
    const element3 = document.getElementById('infor');

    const li_des = document.getElementById('li-des');
    const li_review = document.getElementById('li-review');

    if (infor === 'des') {
      this.checkInfor = 'des';
      element2.style.display = 'none';
      li_review?.attributes.removeNamedItem('class');
      li_review?.setAttribute(
        'class',
        'text-gray-500 font-semibold text-sm hover:bg-gray-100 py-3 px-8 cursor-pointer transition-all'
      );
      li_des?.setAttribute(
        'class',
        'text-gray-800 font-semibold text-sm bg-gray-100 py-3 px-8 border-b-2 border-gray-800 cursor-pointer transition-all'
      );
    }
    if (infor === 'review') {
      this.checkInfor = 'review';
      element2.style.display = 'block';
      li_des?.attributes.removeNamedItem('class');
      li_des?.setAttribute(
        'class',
        'text-gray-500 font-semibold text-sm hover:bg-gray-100 py-3 px-8 cursor-pointer transition-all'
      );
      li_review?.setAttribute(
        'class',
        'text-gray-800 font-semibold text-sm bg-gray-100 py-3 px-8 border-b-2 border-gray-800 cursor-pointer transition-all'
      );
    }
    if (infor === 'information') {
      this.checkInfor = 'information';
    }
  }
  getCommentByProductId(productId: number) {
    debugger;
    this.checkLoad = false;
    this.productService.getCommentByProductId(productId).subscribe({
      next: (response: any) => {
        debugger;
        response.forEach((comment: Comment) => {
          comment.images.forEach((image: CommentImage) => {
            image.imageUrl = `${environtment.apiBaseUrl}/comments/viewImages/${image.imageUrl}`;
          });
        });
        this.comments = response;
        this.checkLoad = true;
      },
      complete: () => {
        debugger;
      },
      error: (response) => {
        debugger;
        console.log(response);
      },
    });
  }

  checkLoader(): boolean {
    if (this.product && this.selectedSize) {
      return true;
    }
    return false;
  }
  buyNow() {
    debugger;
    if (this.product) {
      this.cartService.addToCart(
        this.productId,
        this.quantity,
        this.selectedSize
      );
      this.route.navigate(['/cart']);
    } else {
      console.log('Cannot add cart because product is not available');
    }
  }
  getStartoAddComment(star: number) {
    this.commentRating = star;
  }
  onFileSelected(event: any) {
    const input = event.target as HTMLInputElement;
    const fileNamesDiv = document.getElementById('fileNames');
    if (input.files && fileNamesDiv) {
      const files = input.files;
      const fileNames = [];
      for (let i = 0; i < files.length; i++) {
        fileNames.push(files[i].name);
      }
      fileNamesDiv.innerHTML = fileNames.join('<br>');
    }
    const files = event.target.files;
    this.commentImages = []; // Giả sử commentImages là một mảng để lưu trữ nhiều hình ảnh
    Array.from(files).forEach((file: unknown) => {
      const reader = new FileReader();
      reader.readAsDataURL(file as File);
      reader.onload = () => {
        // Thêm mỗi hình ảnh đã được đọc vào mảng commentImages
        this.commentImages.push(file as File);
      };
    });
  }
  addComment() {
    let commentDto = new CommentDTO({
      content: this.commentContent,
      product_id: this.product.id,
      user_id: this.user_id,
      rating: this.commentRating,
    });
    this.commentService.createComment(commentDto).subscribe({
      next: (response: any) => {
        debugger;
        console.log('Create comment response:', response);
        let commentId = response.id;
        this.commentService
          .uploadImageCommment(commentId, this.commentImages)
          .subscribe({
            next: (response: any) => {
              debugger;
              console.log('Upload image response:', response);
              alert('Comment success');
              this.checkLoad = true;
            },
            complete: () => {
              this.commentImages = [];
              this.commentContent = '';
              this.commentRating = 0;
              this.getCommentByProductId(this.product.id);
            },
            error: (error) => {
              debugger;
              console.error('Upload image error:', error);
            },
          });
      },
      complete: () => {
        debugger;
      },
      error: (error) => {
        debugger;
        console.error('Create comment error:', error);
      },
    });
  }
}
