import { Role } from '../models/role';

export interface UserResponse {
  id: number;
  full_name: string;
  phone_number: string;
  address: string;
  date_of_birth: Date;
  is_active: boolean;
  facebook_account_id: number;
  google_account_id: number;
  role: Role;
}
