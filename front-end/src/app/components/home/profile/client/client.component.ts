import { Component, Input, OnInit } from '@angular/core';
import { ClientDetail } from 'src/models/clientDetail/client-detail';

@Component({
  selector: 'client',
  templateUrl: './client.component.html',
})
export class ClientComponent implements OnInit {
  @Input() clients: ClientDetail[] = [];
  constructor() { }

  ngOnInit(): void {
  }

}
