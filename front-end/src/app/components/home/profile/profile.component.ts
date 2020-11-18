import { Component, OnInit } from '@angular/core';
import { UserProfile } from 'src/models/user/user-profile.model';
import { ProfileService } from 'src/services/profile/profile.service';

@Component({
  selector: 'profile',
  templateUrl: './profile.component.html',
})
export class ProfileComponent implements OnInit {
  typeSelect: number = 1;
  userProfile: UserProfile = new UserProfile();
  constructor(private profileService: ProfileService) {}

  ngOnInit(): void {
    this.getProfile();
  }

  getProfile(): void {
    this.profileService.getProfile().subscribe(
      (res) => {
        this.userProfile = res;
      },
      (err) => {
        console.log(err);
      }
    );
  }
}
