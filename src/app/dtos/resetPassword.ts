export class ResetPassword {
  phoneNumber: string;
  password: string;
  constructor(Data: any) {
    this.phoneNumber = Data.phoneNumber;
    this.password = Data.password;
  }
}
