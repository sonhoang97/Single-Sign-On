import { Component, Input, OnInit, TemplateRef } from '@angular/core';
import { ClientDetail } from 'src/models/clientDetail/client-detail';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { ClientDetailService } from 'src/services/client-detail/client-detail.service';
import { ToastrService } from 'ngx-toastr';
declare var $: any;

@Component({
  selector: 'client',
  templateUrl: './client.component.html',
})
export class ClientComponent implements OnInit {
  @Input() clients: ClientDetail[] = [];

  indexDefault = 0;
  selectedClient = false;
  modalRefReset: BsModalRef;
  modalRefDelete: BsModalRef;

  fieldsRedirectUri: string[] = [];

  tooltipServer =
    'Users will be redirected to this path after they have authenticated with Google. The path will be appended with the authorization code for access, and must have a protocol. It can’t contain URL fragments, relative paths, or wildcards, and can’t be a public IP address.';
  tooltipJs =
    "The HTTP origins that host your web application. This value can't contain wildcards or paths. If you use a port other than 80, you must specify it. For example: https://example.com:8080";

  constructor(
    private modalService: BsModalService,
    private clientService: ClientDetailService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {}

  getItemClient(index: number): void {
    console.log(this.clients[index]);
    this.indexDefault = index;
    this.selectedClient = true;
  }

  openModalReset(isReset: TemplateRef<any>) {
    this.modalRefReset = this.modalService.show(isReset, { class: 'modal-md' });
  }
  openModalDelete(isDelete: TemplateRef<any>) {
    this.modalRefDelete = this.modalService.show(isDelete, {
      class: 'modal-md',
    });
  }

  updateClient(): void {
    this.getRedirectUri();
    this.clientService
      .updateClient(
        this.clients[this.indexDefault].clientId,
        this.clients[this.indexDefault].redirectUri
      )
      .subscribe(
        (res) => {
          this.refresh();
        },
        (err) => {
          this.toastr.warning('Something wrong!');
          console.log(err);
        }
      );
  }

  updateClientSecret(): void {
    this.clientService
      .updateClientSecret(this.clients[this.indexDefault].clientId)
      .subscribe(
        (res) => {
          this.refresh();
        },
        (err) => {
          this.toastr.warning('Something wrong!');
          console.log(err);
        }
      );
  }

  deleteClient(): void {
    this.clientService
      .deleteClient(this.clients[this.indexDefault].clientId)
      .subscribe(
        (res) => {
          this.refresh();
        },
        (err) => {
          this.toastr.warning('Something wrong!');
          console.log(err);
        }
      );
  }

  addRedirect(): void {
    let itemRedirect = '';
    this.clients[this.indexDefault].redirectUri.push(itemRedirect);
  }

  deletedRedirect(event: Event): void {
    let elementId: string = (event.target as Element).id;
    let id = elementId.slice(22, 23);
    document.getElementById('client_item_' + id).remove();
  }

  getRedirectUri(): void {
    for (
      let i = 0;
      i < this.clients[this.indexDefault].redirectUri.length;
      i++
    ) {
      this.clients[this.indexDefault].redirectUri[i] = $(
        '#client_redirectUri_' + i
      ).val();
    }
    this.clients[this.indexDefault].redirectUri = this.clients[
      this.indexDefault
    ].redirectUri.filter((item) => item != undefined);
  }

  refresh(): void {
    window.location.reload();
  }
}
