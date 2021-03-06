import { Component, Input, OnInit } from '@angular/core';
import { ClientDetail } from 'src/models/clientDetail/client-detail';
import { ClientDetailService } from 'src/services/client-detail/client-detail.service';

@Component({
  selector: 'credentials-admin',
  templateUrl: './credentials-admin.component.html',
})
export class CredentialsAdminComponent implements OnInit {
  @Input() authorities = [];
  config: any;
  pageIndex = 1;
  pageSize = 10;
  totalCount: number;
  searchString: string = '';

  clients: ClientDetail[] = [];

  isLoading = true;
  constructor(private clientDetailService: ClientDetailService) {}

  ngOnInit(): void {
    this.getClients('', this.pageIndex, this.pageSize);
    this.config = {
      itemsPerPage: 10,
      currentPage: 1,
      totalItems: this.totalCount,
    };
  }

  searchClients(): void {
    this.pageIndex = 1;
    this.config.currentPage = 1;

    this.getClients(this.searchString, this.pageIndex, this.pageSize);
  }

  getClients(searchString: string, pageIndex, pageSize): void {
    this.isLoading = true;
    pageIndex -= 1;
    this.clientDetailService
      .getClients(searchString, 1, pageIndex, pageSize)
      .subscribe(
        (res) => {
          this.isLoading = false;
          this.clients = res.source;
          this.totalCount = res.totalCount;
          this.config.totalItems = res.totalCount;
        },
        (err) => {
          this.isLoading = false;
          console.log(err);
        }
      );
  }

  pageChanged(event) {
    this.config.currentPage = event;
    this.pageIndex = event;
    this.getClients('', this.pageIndex, this.pageSize);
  }
}
