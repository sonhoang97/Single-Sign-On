import { Component, OnInit } from '@angular/core';
import { ThemePalette } from '@angular/material/core';
import { LsHelper } from 'src/app/commons/helpers/ls.helper';

@Component({
  selector: 'home-admin',
  templateUrl: './home-admin.component.html',
})
export class HomeAdminComponent implements OnInit {
  typeSelect: number = 1;
  color: ThemePalette = 'accent';
  checked = false;
  disabled = false;
  
  authorities = [];
  constructor() {}

  ngOnInit(): void {
    this.getAuthorities();
   }
  
  getAuthorities(): void{
    this.authorities = LsHelper.getAuthoritiesFromToken();
  }
  
}
