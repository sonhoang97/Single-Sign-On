import { Component, OnInit } from '@angular/core';
import { ClientDetail } from 'src/models/clientDetail/client-detail';
import { UserProfile } from 'src/models/user/user-profile.model';
import { ProfileService } from 'src/services/profile/profile.service';

@Component({
  selector: 'profile',
  templateUrl: './profile.component.html',
})
export class ProfileComponent implements OnInit {
  typeSelect: number = 1;
  userProfile: UserProfile = new UserProfile();
  lsClient: ClientDetail[] = [];
  constructor(private profileService: ProfileService) {}

  ngOnInit(): void {
    this.getProfile();
  }

  getProfile(): void {
    this.profileService.getProfile().subscribe(
      (res) => {
        this.userProfile = res;
        this.lsClient = this.userProfile.lsClientDetail;
        console.log(this.lsClient);
      },
      (err) => {
        console.log(err);
      }
    );
  }
}
