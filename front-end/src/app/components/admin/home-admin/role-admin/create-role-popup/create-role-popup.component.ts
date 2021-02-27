import { Component, EventEmitter, OnInit, TemplateRef } from '@angular/core';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { ToastrService } from 'ngx-toastr';
import { Messages } from 'src/app/commons/consts/message.const';
import { Permission } from 'src/models/role/permission.model';
import { Role } from 'src/models/role/role.model';
import { RoleService } from 'src/services/role/role.service';

@Component({
  selector: 'create-role-popup',
  templateUrl: './create-role-popup.component.html',
})
export class CreateRolePopupComponent implements OnInit {
  lsAllPermissions: Permission[] = [];

  newPermissions: Permission[] = [];

  newRole = new Role();

  hasClick = false;

  modalRefConfirm: BsModalRef;

  public event: EventEmitter<any> = new EventEmitter();
  constructor(
    public bsModalRef: BsModalRef,
    private roleService: RoleService,
    private modalService: BsModalService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {}

  openModalConfirm(isCreate: TemplateRef<any>) {
    this.modalRefConfirm = this.modalService.show(isCreate, {
      class: 'modal-md mt-5',
    });
  }

  createNewRole(): void {
    this.roleService.createNewRole(this.newRole).subscribe(
      (res) => {
        this.toastr.success(Messages.SUCCESS.SUCCESS);
        this.triggerEvent(true);
        this.modalRefConfirm.hide();
        this.bsModalRef.hide();
      },
      (err) => {
        this.modalRefConfirm.hide();
        if (err.status == 409) {
          this.toastr.warning('This role name already exist!');
        }
        console.log(err);
      }
    );
  }

  triggerEvent(item: boolean) {
    this.event.emit({ data: item });
  }
}
