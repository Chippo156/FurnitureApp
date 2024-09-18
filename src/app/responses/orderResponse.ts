import { OrderDetail } from '../models/orderDetail';

export interface OrderResponse {
  id: number;
  user_id: number;
  full_name: string;
  email: string;
  phone_number: string;
  address: string;
  note: string;
  status: string;
  order_date: Date;
  total_money: number;
  shipping_method: string;
  payment_method: string;
  shipping_address: string;
  shipping_date: Date;
  tracking_number: string;
  is_active: boolean;
  order_details: OrderDetail[];
}
