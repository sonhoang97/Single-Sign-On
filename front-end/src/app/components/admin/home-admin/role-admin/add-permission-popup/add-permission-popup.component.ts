import { Component, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { ToastrService } from 'ngx-toastr';
import { Messages } from 'src/app/commons/consts/message.const';
import { Permission } from 'src/models/role/permission.model';
import { Role } from 'src/models/role/role.model';
import { RoleService } from 'src/services/role/role.service';

@Component({
  selector: 'add-permission-popup',
  templateUrl: './add-permission-popup.component.html',
})
export class AddPermissionPopupComponent implements OnInit {
  lsAllPermissions: Permission[] = [];
  role: Role = new Role();

  restOfPermissions: Permission[] = [];

  constructor(
    public bsModalRef: BsModalRef,
    private roleService: RoleService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {

    this.getTheRestOfPermission();
  }

  getTheRestOfPermission(): void {
    this.restOfPermissions = Object.assign([], this.lsAllPermissions);
    this.role.permissions.forEach((permission) => {
      this.lsAllPermissions.forEach((per) => {
        if (per.id == permission.id) {
          const index = this.restOfPermissions.indexOf(per, 0);
          if (index > -1) {
            this.restOfPermissions.splice(index, 1);
          }
        }
      });
    });
  }

  submit(): void {
    console.log(this.role);
    this.roleService.updateRole(this.role).subscribe(
      (res) => {
        this.bsModalRef.hide();
        this.toastr.success(Messages.SUCCESS.SUCCESS);
      },
      (err) => {
        console.log(err);
      }
    );
  }
  
}
