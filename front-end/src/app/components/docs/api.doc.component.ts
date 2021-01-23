import { Component, OnInit } from '@angular/core';
declare const SwaggerUIBundle: any;
@Component({
  selector: 'app-swagger-ui',
  templateUrl: './api.doc.component.html',
})
export class ApiDocComponent implements OnInit {
  title = 'angular-swagger-ui-example';

  ngOnInit(): void {
    const ui = SwaggerUIBundle({
      dom_id: '#swagger-ui',
      layout: 'BaseLayout',
      presets: [
        SwaggerUIBundle.presets.apis,
        SwaggerUIBundle.SwaggerUIStandalonePreset,
      ],
      url: 'http://localhost:8083/api/doc',
      docExpansion: 'none',
      operationsSorter: 'alpha',
    });
  }
}
