import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Messages } from 'src/app/commons/consts/message.const';
import { ClientDetail } from 'src/models/clientDetail/client-detail';
import { ClientDetailService } from 'src/services/client-detail/client-detail.service';
declare var $: any;

@Component({
  selector: 'regist-client',
  templateUrl: './regist-client.component.html',
})
export class RegistClientComponent implements OnInit {
  clientId: string;
  redirectUri: string;
  url: string;
  clientDetail: ClientDetail;
  isView: boolean = false;

  redirectsUri: string[] = [];
  fieldsRedirectUri: string[] = [''];
  constructor(
    private clientDetailService: ClientDetailService,
    private toastr: ToastrService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // this.url =
    //   'http://localhost:8083/oauth/authorize?client_id=' +
    //   this.clientDetail.clientId +
    //   '&response_type=code&redirect_uri=' +
    //   this.clientDetail.redirectUri +
    //   '&scope=WRITE';
  }

  submitRegister(type: number): void {
    this.redirectsUri = [];
    if (!this.clientId) {
      return;
    }
    if (type == 1) {
      if ( !this.redirectUri) {
        return;
      }
      this.redirectsUri.push(this.redirectUri);
    } else if (type == 2) {
      for (let i = 0; i < this.fieldsRedirectUri.length; i++) {
        this.redirectsUri[i] = $('#redirect_' + i).val();
      }
      this.redirectsUri = this.redirectsUri.filter((item) => item != undefined);
    }
    this.clientDetailService
      .register(this.clientId, this.redirectsUri)
      .subscribe(
        (res) => {
          this.clientDetail = res;
          this.isView = true;
          this.toastr.success(Messages.SUCCESS.SUCCESS);
        },
        (err) => {
          if ((err.status = 409)) {
            this.toastr.warning(err.error);
          } else {
            this.toastr.warning('Something wrong!');
          }
          this.isView = false;
          console.log(err);
        }
      );
  }

  addRedirect(): void {
    let itemRedirect = '';
    this.fieldsRedirectUri.push(itemRedirect);
  }

  deletedRedirect(event: Event): void {
    let elementId: string = (event.target as Element).id;
    let id = elementId.slice(12, 13);
    document.getElementById('item_' + id).remove();
  }
}
