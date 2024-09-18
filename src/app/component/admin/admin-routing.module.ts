import { Router, RouterModule, Routes } from '@angular/router';
import { AdminComponent } from './admin.component';
import { OrderAdminComponent } from './order/order.admin.component';
import { OrderDetailsAdminComponent } from './order/orderDetail/orderDetails.admin.component';
import { CategoryAdminComponent } from './category/category.admin';
import { ProductAdminComponent } from './product/product.component';
import { NgModule } from '@angular/core';
import { ProductDetailsAdminComponent } from './product/product-details/product-details.admin.component';
import { UserAdminComponent } from './user/user.admin.component';

const routes: Routes = [
  {
    path: 'admin',
    component: AdminComponent,
    children: [
      {
        path: 'orders',
        component: OrderAdminComponent,
      },
      {
        path: 'orders/:id',
        component: OrderDetailsAdminComponent,
      },
      {
        path: 'categories',
        component: CategoryAdminComponent,
      },
      {
        path: 'products',
        component: ProductAdminComponent,
      },
      {
        path: 'products/:id',
        component: ProductDetailsAdminComponent,
      },
      {
        path: 'users',
        component: UserAdminComponent,
      },
    ],
  },
];
@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AdminRoutingModule {}
