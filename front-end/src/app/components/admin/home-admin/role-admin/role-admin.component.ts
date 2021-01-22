import { Component, OnInit } from '@angular/core';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { Permission } from 'src/models/role/permission.model';
import { Role } from 'src/models/role/role.model';
import { PermissionServiceService } from 'src/services/permission/permission-service.service';
import { RoleService } from 'src/services/role/role.service';
import { AddPermissionPopupComponent } from './add-permission-popup/add-permission-popup.component';

@Component({
  selector: 'role-admin',
  templateUrl: './role-admin.component.html',
})
export class RoleAdminComponent implements OnInit {
  lsRoles: Role[] = [];
  isLoading = true;

  lsAllPermissions: Permission[] = [];

  bsModalRef: BsModalRef;

  constructor(
    private roleService: RoleService,
    private permissionService: PermissionServiceService,
    private modalService: BsModalService
  ) {}

  ngOnInit(): void {
    this.getAllRoles();
    this.getAllPermissions();
  }

  getAllRoles(): void {
    this.isLoading = true;
    this.roleService.getAllRoles().subscribe(
      (res) => {
        this.isLoading = false;
        this.lsRoles = res;
        console.log(this.lsRoles);
      },
      (err) => {
        this.isLoading = false;
        console.log(err);
      }
    );
  }

  getAllPermissions(): void {
    this.permissionService.getAllPermissions().subscribe(
      (res) => {
        this.lsAllPermissions = res;
        console.log(res);
      },
      (err) => {
        console.log(err);
      }
    );
  }

  addPermissionPopup(): void {
    if(this.lsAllPermissions.length==0){
      return;
    }
    const initialState = {
      lsAllPermissions: this.lsAllPermissions
    };
    this.bsModalRef = this.modalService.show(
        AddPermissionPopupComponent,
        { initialState, class: 'gray modal-lg' }
    );
}
}
