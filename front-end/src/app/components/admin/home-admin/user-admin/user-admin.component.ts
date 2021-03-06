import { Component, Input, OnInit } from '@angular/core';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { LsHelper } from 'src/app/commons/helpers/ls.helper';
import { Role } from 'src/models/role/role.model';
import { UserProfile } from 'src/models/user/user-profile.model';
import { User } from 'src/models/user/user.model';
import { ProfileService } from 'src/services/profile/profile.service';
import { RoleService } from 'src/services/role/role.service';
import { DetailUserPopupComponent } from './detail-user-popup/detail-user-popup.component';

@Component({
  selector: 'user-admin',
  templateUrl: './user-admin.component.html',
})
export class UserAdminComponent implements OnInit {
  @Input() authorities = [];
  bsModalRef: BsModalRef;
  lsAllRoles: Role[] = [];
  selectedRoleId = 0;
  isLoading = true;

  status: number = -1;

  config: any;
  pageIndex = 1;
  pageSize = 10;
  totalCount: number;
  searchString: string = '';


  constructor(
    private profileService: ProfileService,
    private modalService: BsModalService,
    private roleService: RoleService
  ) {}
  users: UserProfile[] = [];

  ngOnInit(): void {
    this.getUsers('', this.status,this.selectedRoleId, this.pageIndex, this.pageSize);
    this.config = {
      itemsPerPage: 10,
      currentPage: 1,
      totalItems: this.totalCount,
    };
    this.getAllRoles();
  }

  getUsers(searchString: string, status: number, roleId: number, pageIndex, pageSize): void {
    this.isLoading = true;
    pageIndex -= 1;
    this.profileService
      .getUsersAdmin(searchString, status,roleId, 1, pageIndex, pageSize)
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
    this.getUsers('', this.status,this.selectedRoleId, this.pageIndex, this.pageSize);
  }

  searchUsers(): void {
    this.pageIndex = 1;
    this.config.currentPage = 1;

    this.getUsers(
      this.searchString,
      this.status,
      this.selectedRoleId,
      this.pageIndex,
      this.pageSize
    );
  }

  detailUserPopup(user: UserProfile): void {
    const initialState = {
      user,
      authorities: this.authorities
    };
    this.bsModalRef = this.modalService.show(DetailUserPopupComponent, {
      initialState,
      class: 'gray modal-md',
    });

    this.bsModalRef.content.event.subscribe((res: any) => {
      this.pageIndex = 1;
      this.status = -1;
      this.selectedRoleId = 0;
      this.getUsers('', this.status, this.selectedRoleId, this.pageIndex, this.pageSize);
    });
  }

  getAllRoles(): void {
    this.roleService.getAllRoles().subscribe(
      (res) => {
        this.lsAllRoles = res;
      },
      (err) => {
        console.log(err);
      }
    );
  }

  onChange(newValue) {
    this.selectedRoleId = newValue;
    console.log(this.selectedRoleId);
  }

}
