import { IsDate, IsNumber, IsPhoneNumber, IsString } from 'class-validator';
export class RegisterDto {
  @IsString()
  fullname: string;
  @IsPhoneNumber()
  phone_number: string;
  @IsString()
  address: string;
  @IsString()
  password: string;
  @IsString()
  retype_password: string;
  @IsDate()
  date_of_birth: Date;
  @IsNumber()
  facebook_account_id: number;
  @IsNumber()
  google_account_id: number;
  @IsNumber()
  role_id: number;
  constructor(data: any) {
    this.fullname = data.fullname;
    this.phone_number = data.phone_number;
    this.address = data.address;
    this.password = data.password;
    this.retype_password = data.retype_password;
    this.date_of_birth = new Date(data.date_of_birth);
    this.facebook_account_id = data.facebook_account_id | 0;
    this.google_account_id = data.google_account_id | 0;
    this.role_id = data.role_id | 1;
  }
}
