import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {AbstractControl} from '@angular/forms';
import {AuthToken} from '../models/authToken';
import {environment} from '../../../environments/environment';
import {SignUpRequest} from '../models/sign.up.request';
import {UserService} from './user.service';
import {User} from '../models/user';

@Injectable({providedIn: 'root'})
export class AuthenticationService {
  private currentAccessTokenSubject: BehaviorSubject<AuthToken>;
  public currentAccessToken: Observable<AuthToken>;

  constructor(private http: HttpClient) {
    const authToken = JSON.parse(localStorage.getItem('authToken')) as AuthToken;
    this.currentAccessTokenSubject = new BehaviorSubject<AuthToken>(authToken);
    this.currentAccessToken = this.currentAccessTokenSubject.asObservable();
  }

  public get isLoggedIn() {
    return this.currentAccessTokenValue.activatedUser && this.currentAccessTokenValue;
  }

  public get currentAccessTokenValue(): AuthToken {
    return this.currentAccessTokenSubject.value;
  }

  login(usernameOrEmail: AbstractControl, password: AbstractControl) {
    return this.http.post<any>(`${environment.apiUrl}/auth/signin`, {usernameOrEmail, password})
      .pipe(map(response => {
        if (response.principal.activatedUser) {
          const authToken = {
            accessToken: response.accessToken,
            role: response.principal.role,
            activatedUser: response.principal.activatedUser
          } as AuthToken;
          localStorage.setItem('authToken', JSON.stringify(authToken));
          this.currentAccessTokenSubject.next(authToken);
        }
        return response;
      }));
  }

  logout() {
    localStorage.removeItem('authToken');
    this.currentAccessTokenSubject.next(null);
  }

  registrate(signUpRequest: SignUpRequest) {
    return this.http.post<any>(`${environment.apiUrl}/auth/signup`, signUpRequest);
  }
}
