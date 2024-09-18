import { Injectable } from '@angular/core';
import { environtment } from '../environments/environment';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { RegisterDto } from '../dtos/registerDto';
import { Observable } from 'rxjs';
import { LoginDTO } from '../dtos/loginDto';
import { UserResponse } from '../responses/userResponse';
import { UserDto } from '../dtos/userDto';
import { ResetPassword } from '../dtos/resetPassword';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private apiUser = `${environtment.apiBaseUrl}/users`;
  private apiRegister = `${environtment.apiBaseUrl}/users/register`;
  private apiLogin = `${environtment.apiBaseUrl}/users/login`;
  private apiConfig = {
    headers: this.createHeaders(),
  };
  private createHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept-Language': 'vi',
      Authorization: '',
    });
  }
  constructor(private http: HttpClient) {}

  register(registerDto: RegisterDto): Observable<any> {
    return this.http.post(this.apiRegister, registerDto, this.apiConfig);
  }

  login(loginDto: LoginDTO): Observable<any> {
    return this.http.post(this.apiLogin, loginDto, this.apiConfig);
  }
  getUserDetails(token: string): Observable<any> {
    return this.http.get(`${environtment.apiBaseUrl}/users/details`, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      }),
    });
  }
  saveUserResponseToLocalStorage(userResponse: UserResponse) {
    try {
      const userResponseJson = JSON.stringify(userResponse);
      localStorage.setItem('user', userResponseJson);
      console.log('User response saved to local storage');
    } catch (e) {
      console.log('Error saving user response to local storage');
    }
  }
  getUserResponseFromLocalStorage(): UserResponse | undefined {
    try {
      const userResponseJson = localStorage.getItem('user');
      if (userResponseJson === null || userResponseJson == undefined)
        return undefined;
      const userResponse: UserResponse = JSON.parse(userResponseJson);
      return userResponse;
    } catch (e) {
      console.log('Error getting user response from local storage');
      return undefined;
    }
  }
  removeUserResponseFromLocalStorage() {
    try {
      localStorage.removeItem('user');
      console.log('User response removed from local storage');
    } catch (e) {
      console.log('Error removing user response from local storage');
    }
  }
  getUsers(keyword: string, page: number, limit: number): Observable<any> {
    const params = new HttpParams()
      .set('keyword', keyword)
      .set('page', page.toString())
      .set('limit', limit.toString());
    return this.http.get<UserResponse[]>(this.apiUser, { params });
  }
  removeUser(id: number): Observable<any> {
    return this.http.delete(`${this.apiUser}/${id}`);
  }
  updateUser(id: number, userResponse: UserResponse) {
    let userDTO: UserDto = {
      fullname: userResponse.full_name,
      phone_number: userResponse.phone_number,
      address: userResponse.address,
      password: 'khong update',
      retype_password: 'khong update',
      facebook_account_id: userResponse.facebook_account_id,
      google_account_id: userResponse.google_account_id,
      role_id: userResponse.role.id,
      date_of_birth: new Date(userResponse.date_of_birth),
    };
    return this.http.put(`${this.apiUser}/${id}`, userDTO);
  }
  updatePassword(phone_number: string, password: string) {
    let resetPasswordDTO: ResetPassword = {
      phoneNumber: phone_number,
      password: password,
    };
    return this.http.put(`${this.apiUser}/reset-password`, resetPasswordDTO, {
      responseType: 'text',
    });
  }
  updateSocialAccount(phoneNumber: string, providerId: string) {
    return this.http.put(
      `${this.apiUser}/update-social-account`,
      { phoneNumber, providerId },
      {
        responseType: 'text',
      }
    );
  }
}
