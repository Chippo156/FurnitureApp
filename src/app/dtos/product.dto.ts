import { Color } from '../models/colors';
import { Size } from '../models/sizes';

export class ProductDTO {
  product_name: string;
  price: number;
  thumbnail: string;
  description: string;
  size: Size;
  color: string;
  category_id: number;
  sale_id: number;
  constructor(data: any) {
    this.product_name = data.product_name;
    this.price = data.price;
    this.thumbnail = data.thumbnail;
    this.description = data.description;
    this.size = data.size;
    this.color = data.color;
    this.category_id = data.category_id;
    this.sale_id = data.sale_id;
  }
}
