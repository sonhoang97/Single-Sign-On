import { Component, OnInit } from '@angular/core';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { Permission } from 'src/models/role/permission.model';
import { Role } from 'src/models/role/role.model';
import { PermissionServiceService } from 'src/services/permission/permission-service.service';
import { RoleService } from 'src/services/role/role.service';
import { AddPermissionPopupComponent } from './add-permission-popup/add-permission-popup.component';
import { CreateRolePopupComponent } from './create-role-popup/create-role-popup.component';

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
      },
      (err) => {
        console.log(err);
      }
    );
  }

  addPermissionPopup(role: Role): void {
    if (this.lsAllPermissions.length == 0) {
      return;
    }
    const initialState = {
      lsAllPermissions: this.lsAllPermissions,
      role: role,
    };
    this.bsModalRef = this.modalService.show(AddPermissionPopupComponent, {
      initialState,
      class: 'gray modal-lg',
    });
  }

  createRolePopup(): void {
 
    const initialState = {
      lsAllPermissions: this.lsAllPermissions,
    };
    this.bsModalRef = this.modalService.show(CreateRolePopupComponent, {
      initialState,
      class: 'gray modal-lg',
    });

    this.bsModalRef.content.event.subscribe((res: any) => {
      this.getAllRoles();
    });

  }
}
