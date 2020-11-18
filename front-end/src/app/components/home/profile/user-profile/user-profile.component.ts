import { Component, Input, OnInit } from '@angular/core';
import { UserProfile } from 'src/models/user/user-profile.model';

@Component({
  selector: 'user-profile',
  templateUrl: './user-profile.component.html',
})
export class UserProfileComponent implements OnInit {
  @Input() userProfile: UserProfile = new UserProfile();
  constructor() { }

  ngOnInit(): void {
  }

}
