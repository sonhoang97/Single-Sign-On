import { Component, OnInit } from '@angular/core';
import { UserProfile } from 'src/models/user/user-profile.model';
import { User } from 'src/models/user/user.model';
import { ProfileService } from 'src/services/profile/profile.service';

@Component({
  selector: 'user-admin',
  templateUrl: './user-admin.component.html',
})
export class UserAdminComponent implements OnInit {
  isLoading = true;

  status:number = -1;

  config: any;
  pageIndex = 1;
  pageSize = 10;
  totalCount: number;
  searchString: string = '';
  constructor(private profileService: ProfileService) {}
  users: UserProfile[] = [];

  ngOnInit(): void {
    this.getUsers('',this.status, this.pageIndex, this.pageSize);
    this.config = {
      itemsPerPage: 10,
      currentPage: 1,
      totalItems: this.totalCount,
    };
  }

  getUsers(searchString: string,status:number, pageIndex, pageSize): void {
    this.isLoading = true;
    pageIndex -= 1;
    this.profileService
      .getUsersAdmin(searchString,status, 1, pageIndex, pageSize)
      .subscribe(
        (res) => {
          this.isLoading = false;
          this.users = res.source;
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
    this.getUsers('',this.status, this.pageIndex, this.pageSize);
  }

  searchUsers(): void{
    this.pageIndex =1;
    this.config.currentPage = 1;

    this.getUsers(this.searchString,this.status, this.pageIndex, this.pageSize);
  }
}
