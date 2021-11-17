export class AuthToken {
  accessToken: string;
  role: string;
  activatedUser: boolean;

  constructor(accessToken: string, role: string, active: boolean) {
    this.accessToken = accessToken;
    this.role = role;
    this.activatedUser = active;
  }
}
