import { Component, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Config } from 'src/app/config';

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
  apiURLAuthorize: string;
  constructor(public bsModalRef: BsModalRef) {}

  ngOnInit(): void {
    this.apiURLAuthorize = Config.getPathToken();
  }

  login(): void {
    this.redirect =
      this.apiURLAuthorize +
      '?response_type=code&client_id=' +
      this.clientId +
      '&redirect_uri=' +
      this.redirectUri +
      '&scope=read';
  }
  loginWithThisCredential(): void {
    this.clientId = 'mobile';
    this.clientSecret = 'pin';
    this.redirectUri = 'http://ng-sso-manager.s3-website-ap-southeast-1.amazonaws.com/oauth/callback';
    this.redirect =this.apiURLAuthorize+
      '?response_type=code&client_id=' +
      this.clientId +
      '&redirect_uri=' +
      this.redirectUri +
      '&scope=READ';
    window.location.href = this.redirect;
  }
}
