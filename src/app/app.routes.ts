import { PreloadAllModules, RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './component/home/home.component';
import { ContactComponent } from './component/contact/contact.component';
import { ProductComponent } from './component/product/product.component';
import { ProductDetailsComponent } from './component/product-details/product-details.component';
import { CartComponent } from './component/cart/cart.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginComponent } from './component/login/login.component';
import { CartDetailComponent } from './component/cart-detail/cart-detail.component';
import { AdminComponent } from './component/admin/admin.component';
import { AdminGuard } from './guards/admin.guard';
import { AuthGuard } from './guards/auth.guard';
import { OrderDetailsAdminComponent } from './component/admin/order/orderDetail/orderDetails.admin.component';
import { RegisterComponent } from './component/register/register.component';
import { OrderComponent } from './component/order/order.component';
import { AccountComponent } from './component/account/account.component';
import { ProductFavoriteComponent } from './component/product-favorite/product-favorite.component';
import { BlogComponent } from './component/blog/blog.component';
import { ForgotPasswordComponent } from './component/forgot-password/forgot-password.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'forgot', component: ForgotPasswordComponent },
  { path: 'contact', component: ContactComponent },
  { path: 'products', component: ProductComponent },
  { path: 'product-favorite', component: ProductFavoriteComponent },
  { path: 'products/:id', component: ProductDetailsComponent },
  { path: 'cart', component: CartComponent },
  {
    path: 'cartDetail',
    component: CartDetailComponent,
    canActivate: [AuthGuard],
  },
  { path: 'account', component: AccountComponent, canActivate: [AuthGuard] },
  { path: 'orders', component: OrderComponent, canActivate: [AuthGuard] },
  { path: 'admin', component: AdminComponent, canActivate: [AdminGuard] },
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, { preloadingStrategy: PreloadAllModules }),
    CommonModule,
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
