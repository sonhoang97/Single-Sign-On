import { Component, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
  selector: 'demo-login-with-ssopopup',
  templateUrl: './demo-login-with-ssopopup.component.html',
})
export class DemoLoginWithSSOPopupComponent implements OnInit {
  clientId: string;
  clientSecret: string;
  redirectUri: string;
  responseType = 'code';
  scope = 'READ,WRITE';
  redirect: string;
  constructor(public bsModalRef: BsModalRef) {}

  ngOnInit(): void {}

  login(): void {
    this.redirect =
      'http://localhost:8083/oauth/authorize?response_type=code&client_id=' +
      this.clientId +
      '&redirect_uri=' +
      this.redirectUri +
      '&scope=read';
  }
  loginWithThisCredential(): void {
    this.clientId = 'mobile';
    this.clientSecret = 'pin';
    this.redirectUri = 'http://localhost:4200/oauth/callback';
    this.redirect =
      'http://localhost:8083/oauth/authorize?response_type=code&client_id=' +
      this.clientId +
      '&redirect_uri=' +
      this.redirectUri +
      '&scope=READ';
    window.location.href = this.redirect;
  }
}
