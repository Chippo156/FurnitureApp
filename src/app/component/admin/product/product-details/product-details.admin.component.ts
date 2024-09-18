import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { environtment } from 'src/app/environments/environment';
import { Product } from 'src/app/models/product';
import { ProductImage } from 'src/app/models/product.image';
import { ProductService } from 'src/app/service/product.service';

@Component({
  selector: 'app-product-details-admin',
  templateUrl: './product-details.admin.component.html',
  styleUrls: ['./product-details.admin.component.scss'],
})
export class ProductDetailsAdminComponent implements OnInit {
  editButton: boolean = false;
  constructor(
    private router: ActivatedRoute,
    private productService: ProductService,
    private route: Router
  ) {}

  product!: Product;
  productId: number = 0;
  quantity: number = 1;
  idDirect: number = 1;
  currentImageIndex: number = 0;
  productImages: File[] = [];
  checkImage: boolean = false;
  ngOnInit(): void {
    debugger;
    this.getProductDetails();
  }

  getProductDetails() {
    this.idDirect = Number(this.router.snapshot.paramMap.get('id'));
    const idParam = this.idDirect;
    if (idParam !== null) {
      this.productId = +idParam;
    }
    if (!isNaN(this.productId)) {
      this.productService.getProductDetails(this.productId).subscribe({
        next: (response: any) => {
          if (response.product_images && response.product_images.length > 0) {
            response.product_images.forEach((product_images: ProductImage) => {
              product_images.image_url = `${environtment.apiBaseUrl}/products/viewImages/${product_images.image_url}`;
            });
          }
          if (response.product_sale === null) {
            response.product_sale = {
              id: 3,
              description: '',
              sale: 0,
              newProduct: true,
              startDate: new Date(),
              endDate: new Date(),
            };
          }

          this.product = response;
          this.showImage(0);
        },
        complete: () => {},
        error: (error) => {
          console.log(error);
        },
      });
    } else {
      console.log('Invalid productId: ', idParam);
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
        index = 0;
      } else {
        if (index >= this.product.product_images.length) {
          index = this.product.product_images.length - 1;
        }
      }
      this.currentImageIndex = index;
    }
  }
  clickEditButton() {
    this.editButton = true;
  }
  saveProduct() {
    debugger;
    this.productService.updateProduct(this.productId, this.product).subscribe({
      next: (response: any) => {
        debugger;
        alert('success');
        this.editButton = false;
      },
      complete: () => {},
      error: (error) => {
        debugger;
        console.log(error);
      },
    });
  }
  thumbnailClick(index: number) {
    this.currentImageIndex = index;
  }
  nextImage() {
    debugger;
    this.showImage(this.currentImageIndex++);
  }
  previousImage() {
    this.showImage(this.currentImageIndex--);
  }
  onFileSelected(event: any) {
    this.checkImage = true;
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
    this.productImages = []; // Giả sử commentImages là một mảng để lưu trữ nhiều hình ảnh
    Array.from(files).forEach((file: unknown) => {
      const reader = new FileReader();
      reader.readAsDataURL(file as File);
      reader.onload = () => {
        // Thêm mỗi hình ảnh đã được đọc vào mảng commentImages
        this.productImages.push(file as File);
      };
    });
  }
  uploadImage() {
    debugger;
    this.productService
      .uploadImageProduct(this.productId, this.productImages)
      .subscribe({
        next: (response: any) => {
          alert('Upload Success');
          this.route.navigate(['/admin/product', this.productId]);
        },
        complete: () => {},
        error: (error) => {
          debugger;
          alert(error.error);
        },
      });
  }
}
