import { Component, OnInit } from '@angular/core';
import { ThemePalette } from '@angular/material/core';

@Component({
  selector: 'home-admin',
  templateUrl: './home-admin.component.html',
})
export class HomeAdminComponent implements OnInit {
  typeSelect: number = 1;
  color: ThemePalette = 'accent';
  checked = false;
  disabled = false;
  constructor() {}

  ngOnInit(): void {}
}
