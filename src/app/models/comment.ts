import { CommentImage } from './comment.image';
export interface Comment {
  userName: string;
  content: string;
  rating: number;
  images: CommentImage[];
}
