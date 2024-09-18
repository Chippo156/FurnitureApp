// import { NgModule } from '@angular/core';
// import { CommonModule } from '@angular/common';
// import { BrowserModule } from '@angular/platform-browser';
// import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
// import { FormsModule, ReactiveFormsModule } from '@angular/forms';
// import { HomeComponent } from './component/home/home.component';
// import { AppRoutingModule } from './app.routes';
// import { HeaderComponent } from './component/header/header.component';
// import { FooterComponent } from './component/footer/footer.component';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HomeComponent } from './component/home/home.component';
import { AppRoutingModule, routes } from './app.routes';
import { HeaderComponent } from './component/header/header.component';
import { FooterComponent } from './component/footer/footer.component';
import { RegisterComponent } from './component/register/register.component';
import { LoginComponent } from './component/login/login.component';
import { CartComponent } from './component/cart/cart.component';
import { ProductComponent } from './component/product/product.component';
import { ProductDetailsComponent } from './component/product-details/product-details.component';
import { BlogComponent } from './component/blog/blog.component';
import { ContactComponent } from './component/contact/contact.component';
import { Router, RouterOutlet } from '@angular/router';
import { AppComponent } from './app.component';
import { TokenInterceptor } from './interceptors/token.interceptor';
import { CartDetailComponent } from './component/cart-detail/cart-detail.component';
import { ProductService } from './service/product.service';
import {
  NgbPopover,
  NgbPopoverConfig,
  NgbPopoverModule,
} from '@ng-bootstrap/ng-bootstrap';
import { OrderAdminComponent } from './component/admin/order/order.admin.component';
import { AdminComponent } from './component/admin/admin.component';
import { CategoryAdminComponent } from './component/admin/category/category.admin';
import { ProductAdminComponent } from './component/admin/product/product.component';
import { OrderDetailsAdminComponent } from './component/admin/order/orderDetail/orderDetails.admin.component';
import { OrderComponent } from './component/order/order.component';
import { AdminModule } from './component/admin/admin.module';
import { ProductDetailsAdminComponent } from './component/admin/product/product-details/product-details.admin.component';
import { AccountComponent } from './component/account/account.component';
import { AlertComponent } from './alert/alert.component';
import { AlertService } from './service/alert.service';
import { ProductFavoriteComponent } from './component/product-favorite/product-favorite.component';
import { AuthConfig, OAuthModule, OAuthService } from 'angular-oauth2-oidc';
import { ForgotPasswordComponent } from './component/forgot-password/forgot-password.component';

@NgModule({
  imports: [
    BrowserModule,
    HttpClientModule,
    ReactiveFormsModule,
    CommonModule,
    FormsModule,
    AppRoutingModule,
    NgOptimizedImage,
    NgbPopover,
    NgbPopoverModule,
    AdminModule,
    OAuthModule.forRoot(),
  ],
  declarations: [
    AlertComponent,
    HomeComponent,
    LoginComponent,
    RegisterComponent,
    FooterComponent,
    HeaderComponent,
    CartComponent,
    CartDetailComponent,
    ProductComponent,
    ProductDetailsComponent,
    BlogComponent,
    ContactComponent,
    AppComponent,
    OrderComponent,
    OrderDetailsAdminComponent,
    ProductDetailsAdminComponent,
    AccountComponent,
    AlertComponent,
    ProductFavoriteComponent,
    ForgotPasswordComponent,
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true,
    },
    [ProductService],
    [AlertService],
  ],
  bootstrap: [
    // CartDetailComponent,
    // ProductDetailsComponent,
    // RegisterComponent,
    // LoginComponent,
    // HomeComponent,
    AppComponent,
    // OrderComponent,
    // ProductComponent,
    // OrderDetailComponent,
  ],
})
export class AppModule {}
