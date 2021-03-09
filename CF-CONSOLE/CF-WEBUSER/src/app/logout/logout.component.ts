import {Component, OnInit} from '@angular/core';
import {CommonService} from '../common/common.service';
import {Router} from '@angular/router';
import {NGXLogger} from 'ngx-logger';
import {SecurityService} from '../auth/security.service';
import {KeycloakService} from "keycloak-angular";
declare var require: any;
let appConfig = require('assets/resources/env/config.json');
@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.css']
})
export class LogoutComponent implements OnInit {

  constructor(private router: Router, private security: SecurityService, private keycloakService: KeycloakService, private log: NGXLogger, private  common: CommonService) {
    this.LogOut();

  }

  ngOnInit() {
  }

  LogOut() {
    this.keycloakService.logout(appConfig['egov_portal_url']);
    //this.common.isLoading = true;
    //this.security.doLogout();
  }

}
