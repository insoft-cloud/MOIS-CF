import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {KeycloakAuthGuard, KeycloakConfig, KeycloakService} from "keycloak-angular";
import {isNullOrUndefined} from "util";
declare var require: any;
let appConfig = require('assets/resources/env/config.json');
let keycloakConfig: KeycloakConfig = {
  url: appConfig['authUrl'],
  realm: appConfig['realm'],
  clientId: appConfig['clientId']
};
@Injectable()
export class AuthGuard extends KeycloakAuthGuard {

  key : boolean;
  constructor(protected router: Router, protected keycloakAngular: KeycloakService) {
    super(router, keycloakAngular);
  }

  isAccessAllowed(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<boolean>  {
    return new Promise(async (resolve, reject) => {
      if (!this.authenticated) {
        //this.keycloakAngular.login();
        window.sessionStorage.removeItem('egovp_user_name');
        window.sessionStorage.removeItem('egovp_user_id');
        window.sessionStorage.removeItem('egovp_org_cd');
        window.sessionStorage.removeItem('egovp_org_name');
        window.sessionStorage.removeItem('egovp_project_id');
        window.sessionStorage.removeItem('egovp_project_name');
        window.sessionStorage.removeItem('egovp_user_se_cd');
        window.sessionStorage.removeItem('returnUrl');
        window.sessionStorage.removeItem('catalog_number');
        window.location.href = appConfig['egov_portal_url'];
        return;
      }
      const requiredRoles = route.data.roles;
      if (!requiredRoles || requiredRoles.length === 0) {
        return resolve(true);
      } else {
        if (!this.roles || this.roles.length === 0) {

          resolve(false);
        }
        let granted: boolean = false;
        for (const requiredRole of requiredRoles) {
          if (this.roles.indexOf(requiredRole) > -1) {
            granted = true;
            break;
          }
        }
        resolve(granted);
      }
    });
  }
}
