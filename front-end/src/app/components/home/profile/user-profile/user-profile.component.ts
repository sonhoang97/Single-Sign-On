import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Messages } from 'src/app/commons/consts/message.const';
import { UserProfile } from 'src/models/user/user-profile.model';
import { ProfileService } from 'src/services/profile/profile.service';

@Component({
  selector: 'user-profile',
  templateUrl: './user-profile.component.html',
})
export class UserProfileComponent implements OnInit {
  @Input() userProfile: UserProfile = new UserProfile();
  editClick = false;
  constructor(
    private profileService: ProfileService,
    private toastr: ToastrService,
    private router: Router
  ) {}

  ngOnInit(): void {}
  editProfile(): void {
    this.editClick = !this.editClick;
  }

  submitEdit(): void {
    if (
      !this.userProfile.firstname ||
      !this.userProfile.lastname ||
      !this.userProfile.email ||
      !this.userProfile.phonenumber
    ) {
      return;
    }

    this.profileService
      .updateProfile(
        this.userProfile.firstname,
        this.userProfile.lastname,
        this.userProfile.email,
        this.userProfile.phonenumber
      )
      .subscribe(
        (res) => {
          this.toastr.success(Messages.SUCCESS.SUCCESS);
          this.refresh();
        },
        (err) => {
          this.toastr.warning(Messages.ERROR.FAIL);
          console.log(err);
        }
      );
  }

  refresh(): void {
    window.location.reload();
  }
}
