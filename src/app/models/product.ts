import { Color } from './colors';
import { Comment } from './comment';
import { ProductImage } from './product.image';
import { Sale } from './sale';
import { Size } from './sizes';

export interface Product {
  id: number;
  name: string;
  price: number;
  thumbnail: string;
  url: string;
  description: string;
  created_at: Date;
  classify_color_id: number;
  category_id: number;
  product_images: ProductImage[];
  product_sale: Sale;
  code_color: string;
  product_sizes: Size[];
}
