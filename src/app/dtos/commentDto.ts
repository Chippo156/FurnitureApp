export class CommentDTO {
  content: string;
  product_id: number;
  user_id: number;
  rating: number;
  constructor(data: any) {
    this.content = data.content;
    this.product_id = data.product_id;
    this.user_id = data.user_id;
    this.rating = data.rating;
  }
}
