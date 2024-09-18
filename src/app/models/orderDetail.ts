import { Product } from './product';

export interface OrderDetail {
  id: number;
  order_id: number;
  product: Product;
  number_of_products: number;
  price: number;
  total_money: number;
  sub_total: number;
}
