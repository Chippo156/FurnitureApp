import { IsString } from 'class-validator';

export class CategoryDTO {
  @IsString()
  category_name: string;
  constructor(data: any) {
    this.category_name = data.category_name;
  }
}
