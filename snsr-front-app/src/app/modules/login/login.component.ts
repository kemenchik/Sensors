import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {first} from 'rxjs/operators';
import {AuthenticationService} from '../../shared/services/authentication.service';
import {SignUpRequest} from '../../shared/models/sign.up.request';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  registrationForm: FormGroup;
  returnUrl: string;
  modalOpened: boolean;
  signUpSuccess: boolean;
  regErrorMessage: string;
  regError: boolean;
  loginError = false;
  logErrorMessage: string;
  proccedRegistration = false;

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthenticationService
  ) {
    if (this.authenticationService.currentAccessTokenValue) {
      this.router.navigate(['/dashboard']);
    }
  }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      usernameOrEmail: ['', [
        Validators.required,
        Validators.minLength(5)
      ]],
      password: ['', [
        Validators.required,
        Validators.minLength(8),
        Validators.pattern(/^[a-zA-Z0-9!@#$%^&*()_]+$/)]
      ]
    });

    this.registrationForm = this.formBuilder.group({
      username: ['', [
        Validators.required,
        Validators.minLength(5)
      ]],
      password: ['', [
        Validators.required,
        Validators.minLength(8),
        Validators.pattern(/^[a-zA-Z0-9!@#$%^&*()_]+$/)]
      ],
      email: ['', [
        Validators.required,
        Validators.email
      ]]
    });

    this.returnUrl = this.route.snapshot.queryParams.returnUrl || '/';
  }

  get loginControls() {
    return this.loginForm.controls;
  }

  get registrationControls() {
    return this.registrationForm.controls;
  }

  showError(msg: string) {
    this.logErrorMessage = msg;
    this.loginError = true;
    setTimeout(() => {
      this.loginError = false;
    }, 5000);
  }

  showSuccess(msg: string) {
    this.logErrorMessage = msg;
    this.signUpSuccess = true;
    setTimeout(() => {
      this.signUpSuccess = false;
    }, 5000);
  }

  registrationError(msg: string) {
    this.regErrorMessage = msg;
    this.regError = true;
    setTimeout(() => {
      this.regError = false;
    }, 5000);
  }

  onSubmit() {
    if (this.loginForm.valid) {
      const {usernameOrEmail, password} = this.loginControls;
      this.authenticationService.login(usernameOrEmail.value, password.value)
        .pipe(first())
        .subscribe(
          (response) => {
            if (response.principal.activatedUser) {
              this.router.navigate(['dashboard']);
            } else { this.showError('Аккаунт не активирован!');
            }
          }, error => {
            this.showError('Неправильные логин/email или пароль!');
          });
    } else {
      if (this.loginForm.controls.usernameOrEmail.invalid &&
        (this.loginForm.controls.usernameOrEmail || this.loginForm.controls.usernameOrEmail)) {
        if (this.loginForm.controls.usernameOrEmail.errors.required) {
          this.showError('Поле логин/email обязательное!');
        }
        if (this.loginForm.controls.usernameOrEmail.errors.minlength) {
          this.showError('Логин должен быть не менее 5 символов');
        }
        if (this.loginForm.controls.usernameOrEmail.errors.pattern) {
          this.showError('В поле логин используются запрещенные символы');
        }
      }
      if (this.loginForm.controls.password.invalid && (this.loginForm.controls.password || this.loginForm.controls.password)) {
        if (this.loginForm.controls.password.errors.required) {
          this.showError('Поле пароль обязательное!');
        }
        if (this.loginForm.controls.password.errors.required && this.loginForm.controls.usernameOrEmail.errors.required) {
          this.showError('Логин и пароль обязательны!');
        }
        if (this.loginForm.controls.password.errors.minlength) {
          this.showError('Пароль должен быть не менее 8 символов');
        }
        if (this.loginForm.controls.password.errors.pattern) {
          this.showError('В поле пароль используются запрещенные символы');
        }
      }
    }
  }

  registration() {
    if (this.registrationForm.valid) {
      this.proccedRegistration = true;
      const request = {
        username: this.registrationControls.username.value,
        password: this.registrationControls.password.value,
        email: this.registrationControls.email.value
      };
      this.authenticationService.registrate(request).subscribe((apiResponse) => {
        if (apiResponse.success) {
          this.proccedRegistration = false;
          this.modalOpened = false;
          this.showSuccess('Вы зарегистрированы! Теперь необходимо активировать аккаунт.');
        } else {
          this.proccedRegistration = false;
          switch (apiResponse.message) {
            case 'Username is already taken!':
              this.registrationError('Логин уже занят!');
              break;
            case 'Email Address already in use!':
              this.registrationError('Email уже занят!');
              break;
          }
        }
      }, (error) => {
        this.proccedRegistration = false;
        this.modalOpened = false;
        this.showError('Регистрация не удалась по непредвиденной ошибке. Возможно, не удалось отправить сообщение на указанную почту');
      });
    }
  }
}
