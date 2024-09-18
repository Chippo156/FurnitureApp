import { IsNumber } from 'class-validator';
import { OrderDetail } from '../models/orderDetail';

export class OrderDTO {
  @IsNumber()
  user_id: number;
  full_name: string;
  email: string;
  phone_number: string;
  address: string;
  note: string;
  order_status: string;
  total_money: number;
  shipping_method: string;
  payment_method: string;
  shipping_address: string;
  tracking_number: string;
  cart_items: { product_id: number; quantity: number }[];
  constructor(Data: any) {
    this.user_id = Data.user_id;
    this.full_name = Data.full_name;
    this.email = Data.email;
    this.phone_number = Data.phone_number;
    this.address = Data.address;
    this.note = Data.note;
    this.order_status = Data.order_status;
    this.total_money = Data.total_money;
    this.shipping_method = Data.shipping_method;
    this.payment_method = Data.payment_method;
    this.shipping_address = Data.shipping_address;
    this.tracking_number = Data.tracking_number;
    this.cart_items = Data.cart_items;
  }
}
