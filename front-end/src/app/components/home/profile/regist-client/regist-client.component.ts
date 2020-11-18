import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { ClientDetail } from 'src/models/clientDetail/client-detail';
import { ClientDetailService } from 'src/services/client-detail/client-detail.service';

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

  submitRegister(): void {
    this.clientDetailService
      .register(this.clientId, this.redirectUri)
      .subscribe(
        (res) => {
          this.clientDetail = res;
          this.isView = true;
        },
        (err) => {
          this.toastr.warning('Something wrong!');
          this.isView = false;
          console.log(err);
        }
      );
  }
}
