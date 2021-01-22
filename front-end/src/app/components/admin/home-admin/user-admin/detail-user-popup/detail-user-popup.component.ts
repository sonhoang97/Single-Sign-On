import { TemplateRef } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { ToastrService } from 'ngx-toastr';
import { Messages } from 'src/app/commons/consts/message.const';
import { Role } from 'src/models/role/role.mdel';
import { UserProfile } from 'src/models/user/user-profile.model';
import { ProfileService } from 'src/services/profile/profile.service';
import { RoleService } from 'src/services/role/profile.service';

@Component({
  selector: 'detail-user-popup',
  templateUrl: './detail-user-popup.component.html',
})
export class DetailUserPopupComponent implements OnInit {
  user: UserProfile = new UserProfile();
  lsAllRoles: Role[] = [];
  modalRefBan: BsModalRef;
  modalRefActive: BsModalRef;

  constructor(
    public bsModalRef: BsModalRef,
    private roleService: RoleService,
    private profileService: ProfileService,
    private toastr: ToastrService,
    private modalService: BsModalService
  ) {}

  ngOnInit(): void {
    this.getAllRoles();
  }

  openModalBan(isBan: TemplateRef<any>) {
    this.modalRefBan = this.modalService.show(isBan, { class: 'modal-md' });
  }

  openModalActive(isActive: TemplateRef<any>) {
    this.modalRefActive = this.modalService.show(isActive, { class: 'modal-md' });
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

  banUser(): void {
    this.profileService.banUser(this.user.username).subscribe(
      (res) => {
        this.bsModalRef.hide();
        this.modalRefBan.hide();
        this.toastr.success(Messages.SUCCESS.SUCCESS);
      },
      (err) => {
        this.toastr.warning(Messages.ERROR.FAIL);
        console.log(err);
      }
    );
  }
  activeUser(): void {
    this.profileService.activeUser(this.user.username).subscribe(
      (res) => {
        this.bsModalRef.hide();
        this.modalRefActive.hide();
        this.toastr.success(Messages.SUCCESS.SUCCESS);
      },
      (err) => {
        this.bsModalRef.hide();
        this.toastr.warning(Messages.ERROR.FAIL);
        console.log(err);
      }
    );
  }
}
