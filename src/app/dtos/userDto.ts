export class UserDto {
  fullname: string;
  phone_number: string;
  address: string;
  password: string;
  retype_password: string;
  date_of_birth: Date;
  facebook_account_id: number;
  google_account_id: number;
  role_id: number;
  constructor(Data: any) {
    this.fullname = Data.fullname;
    this.phone_number = Data.phone_number;
    this.address = Data.address;
    this.password = Data.password;
    this.retype_password = Data.retype_password;
    this.date_of_birth = Data.date_of_birth;
    this.facebook_account_id = Data.facebook_account_id;
    this.google_account_id = Data.google_account_id;
    this.role_id = Data.role_id;
  }
}
