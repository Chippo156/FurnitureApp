import { Inject, Injectable } from '@angular/core';
import { environtment } from '../environments/environment';
import { Observable } from 'rxjs';
import { Provinces } from '../models/provinces';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class ProvinceService {
  private getProvinceAPI: string = `${environtment.apiBaseUrl}/users/provinces`;
  private getAllProvinceAPI: string = `${environtment.apiBaseUrl}/users/get-provinces`;
  private getDistrictsAPI: string = `${environtment.apiBaseUrl}/users/get-districts`;
  private getCommunesAPI: string = `${environtment.apiBaseUrl}/users/get-communes`;
  constructor(private http: HttpClient) {}
  getProvinces(): Observable<any> {
    return this.http.get<Provinces[]>(this.getProvinceAPI);
  }
  getAllProvince(): Observable<any> {
    return this.http.get(this.getAllProvinceAPI);
  }
  getDistricts(provinceId: number): Observable<any> {
    return this.http.get(`${this.getDistrictsAPI}/${provinceId}`);
  }
  getCommunes(districtId: number): Observable<any> {
    return this.http.get(`${this.getCommunesAPI}/${districtId}`);
  }
}
