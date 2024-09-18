import { NgModule } from '@angular/core';
import { AdminComponent } from './admin.component';
import { OrderComponent } from '../order/order.component';
import { CategoryAdminComponent } from './category/category.admin';
import { OrderDetailsAdminComponent } from './order/orderDetail/orderDetails.admin.component';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppRoutingModule } from 'src/app/app.routes';
import { NgbPopover, NgbPopoverModule } from '@ng-bootstrap/ng-bootstrap';
import { AdminRoutingModule } from './admin-routing.module';
import { OrderAdminComponent } from './order/order.admin.component';
import { ProductAdminComponent } from './product/product.component';
import { UserAdminComponent } from './user/user.admin.component';

@NgModule({
  declarations: [
    AdminComponent,
    OrderAdminComponent,
    CategoryAdminComponent,
    ProductAdminComponent,
    UserAdminComponent,
  ],
  imports: [
    ReactiveFormsModule,
    CommonModule,
    FormsModule,
    AdminRoutingModule,
    NgOptimizedImage,
    NgbPopover,
    NgbPopoverModule,
  ],
  providers: [],
  bootstrap: [],
})
export class AdminModule {}
