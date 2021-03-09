import {KeycloakConfig, KeycloakService} from 'keycloak-angular';
import {environment} from "../../../environments/environment";
import {CommonService} from "../../common/common.service";
declare var require: any;
let appConfig = require('assets/resources/env/config.json');
// Add here your keycloak setup infos
let keycloakConfig: KeycloakConfig = {
  url: appConfig['authUrl'],
  realm: appConfig['realm'],
  clientId: appConfig['clientId']
};



export function initializer(keycloak: KeycloakService): () => Promise<any> {
  return (): Promise<any> => {
    return new Promise(async (resolve, reject) => {
      try {
        await keycloak.init({
          config: keycloakConfig,
          initOptions: {
            onLoad : 'check-sso',
            checkLoginIframe: false
          },
          enableBearerInterceptor: false,
          bearerExcludedUrls: []
        });
        if(await keycloak.isLoggedIn()){
          try{
            window.sessionStorage.setItem('egovp_user_id',keycloak.getUsername());
          } catch (e) {
            console.log(e.toString());
          }
        }
        resolve();
      } catch (error) {
        reject(error);
      }
    });
  };
}
