import { Component, OnInit } from '@angular/core';
import { Config } from '../../config';
import { PathController } from '../../commons/consts/path-controller.const';

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

      url: Config.getPath(PathController.Doc),
      docExpansion: 'none',
      operationsSorter: 'alpha',
    });
  }
}
