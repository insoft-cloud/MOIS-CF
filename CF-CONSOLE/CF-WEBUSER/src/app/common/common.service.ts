import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/map';
import {Observable} from 'rxjs/Observable';

import {NGXLogger} from 'ngx-logger';
import {Param} from "../index/login/login.component";
import {Router} from "@angular/router";
import {isNullOrUndefined} from "util";
import {LoginService} from "../index/login/login.service";
import {KeycloakService} from "keycloak-angular";

declare var require: any;
let appConfig = require('assets/resources/env/config.json');
declare var $: any;

@Injectable()
export class CommonService {
  isLoading = false;
  headers: HttpHeaders;
  fileheaders: HttpHeaders;
  private gateway = '';
  defaultLang = 'ko';
  useLang = 'ko';


  constructor(public http: HttpClient, public router: Router, public log: NGXLogger, private keycloak : KeycloakService) {
    this.headers = new HttpHeaders()
      .set('Content-Type', 'application/json')
      .set('X-Broker-Api-Version', '2.4')
      .set('X-Requested-With', 'XMLHttpRequest')
      .set('Authorization', 'Basic YWRtaW46b3BlbnBhYXN0YQ==')
  }


  extractData(res: Response) {
    let body = res.json();
    return body || {};
  }

  doStorageGet(url: string, token: string) {
    if (token == null) {
      token = '';
    }
    let storageheader = new HttpHeaders()
      .set('Authorization', 'Basic YWRtaW46b3BlbnBhYXN0YQ==');
    return this.http.get(url, {
      headers: storageheader.set('cf-Authorization', token).set('Authorization', 'Basic YWRtaW46b3BlbnBhYXN0YQ=='),
      responseType: "blob"
    });
  }

  doGet(url: string, token: string) {
    if (token == null) {
      token = '';
    }
    return this.http.get(url, {
      headers: this.headers.set('cf-Authorization', token).set('Authorization', 'Basic YWRtaW46b3BlbnBhYXN0YQ==')
    });
  }


  doPost(url: string, body: any, token: string) {
    if (token == null) {
      token = '';
    }
    return this.http.post(url, body, {
      headers: this.headers.set('cf-Authorization', token).set('Authorization', 'Basic YWRtaW46b3BlbnBhYXN0YQ==')
    });
  }

  doFilePost(url: string, body: any, token: string) {
    if (token == null) {
      token = '';
    }
    this.fileheaders = new HttpHeaders();
    this.fileheaders.set('Content-Type', 'multipart/form-data');
    return this.http.post(url, body, {
      headers: this.fileheaders.set('cf-Authorization', token).set('Authorization', 'Basic YWRtaW46b3BlbnBhYXN0YQ==')
    });
  }

  doCommonPost(url: string, body: any) {
    return this.http.post(url, body, {
      headers: this.headers.set('Authorization', 'Basic YWRtaW46b3BlbnBhYXN0YQ==')
    });
  }

  doPut(url: string, body: any, token: string) {
    if (token == null) {
      token = '';
    }
    return this.http.put(url, body, {
      headers: this.headers.set('cf-Authorization', token).set('Authorization', 'Basic YWRtaW46b3BlbnBhYXN0YQ==')
    });
  }

  doPutMail(url: string, authorization, params: any, token) {
    if (token == null) {
      token = '';
    }
    return this.http.put(url, params, {
      headers: this.headers.set('cf-Authorization', token).set('Authorization', 'Basic YWRtaW46b3BlbnBhYXN0YQ==')
    });
  }


  doDelete(url: string, params: any, token: string) {
    if (token == null) {
      token = '';
    }
    return this.http.delete(url, {
      params: params,
      headers: this.headers.set('cf-Authorization', token).set('Authorization', 'Basic YWRtaW46b3BlbnBhYXN0YQ==')
    });
  }

  doDeleteNoParams(url: string, token: string) {
    if (token == null) {
      token = '';
    }
    return this.http.delete(url, {
      headers: this.headers.set('cf-Authorization', token).set('Authorization', 'Basic YWRtaW46b3BlbnBhYXN0YQ==')
    });
  }

  signOut() {
    this.removeItems();
    window.location.href = appConfig['logoutUrl'] +
      '?redirect=' +appConfig['egov_portal_url'] + '&client_id=' + appConfig['clientId'];

    //this.removeItems();
    //window.location.href = appConfig['egov_portal_url'];
    // setTimeout(()=>{
    //   this.router.navigate([appConfig['logoutredirectUri']]);
    // },2000)
  }

  signOn(redirect : boolean) {
    let param = {
      userName : window.sessionStorage.getItem('egovp_user_id')
    };
    this.doPost('/portalapi/v3/user/keycloak',param,null).subscribe(data => {
      let token = data['tokenData'];
      sessionStorage.setItem('cf_user_guid', data['userID']);
      this.saveToken(token['token_type'],token['access_token'],token['refresh_token'],token['expires_in'],token['scope'],'OAUTH');
      if(redirect){
        this.doGo(window.sessionStorage.getItem('returnUrl'));
      }
    }, error => {
      this.router.navigate(['error']);
    });
}

  doGo(returnUrl: string) {
    if (returnUrl == null || returnUrl == '/' ) {
      returnUrl = sessionStorage.getItem('returnUrl');
      this.router.navigate([ returnUrl ]);
    } else {
      let params = this.setParams(returnUrl);
      this.router.navigate([this.setUrl(returnUrl)], {queryParams: params});
    }
  }


  clearSession(){
    this.removeItems();
  }


  private removeItems() {
    window.sessionStorage.removeItem('cf_token_type');
    window.sessionStorage.removeItem('cf_token');
    window.sessionStorage.removeItem('cf_refresh_token');
    window.sessionStorage.removeItem('cf_scope');
    window.sessionStorage.removeItem('cf_user_guid');
    window.sessionStorage.removeItem('cf_user_id');
    window.sessionStorage.removeItem('cf_user_email');
    window.sessionStorage.removeItem('cf_previous_logon_time');
    window.sessionStorage.removeItem('login_type');
    window.sessionStorage.removeItem('user_id');
    window.sessionStorage.removeItem('user_name');
    window.sessionStorage.removeItem('status');
    window.sessionStorage.removeItem('tell_phone');
    window.sessionStorage.removeItem('zip_code');
    window.sessionStorage.removeItem('address');
    window.sessionStorage.removeItem('admin_yn');
    window.sessionStorage.removeItem('img_path');
    window.sessionStorage.removeItem('expires_in');
    //window.sessionStorage.removeItem('catalog_number');
    window.sessionStorage.removeItem('sessionTimeout');
    window.sessionStorage.removeItem('expire_date');
    window.sessionStorage.removeItem('token');
  }

  public saveToken(token_type: any, token: any, refresh_token: any, expires_in: any, scope: any, login_type: any) {

    window.sessionStorage.setItem('cf_token_type', token_type);
    window.sessionStorage.setItem('cf_token', token);
    window.sessionStorage.setItem('cf_refresh_token', refresh_token);
    window.sessionStorage.setItem('cf_scope', scope);
    window.sessionStorage.setItem('login_type', login_type);
    window.sessionStorage.setItem('expires_in', expires_in);

    let token_time = new Date();
    /*
     * expires_in은 몇 초 후 만료를 뜻함
     * 10초전 통신 시간을 고려해 logout
     */
    token_time.setSeconds(token_time.getSeconds() + expires_in - 10);
    // token_time.setSeconds(token_time.getSeconds() + expires_in - 10));
    //this.log.debug('Token Expire Time :' + token_time.getHours() + ":" + token_time.getMinutes() + ":" + token_time.getSeconds());
    window.sessionStorage.setItem('expire_date', token_time.getTime().toString());


    /*
     * Session Time
     */
    let sessionTime = new Date();
    sessionTime.setMinutes(sessionTime.getMinutes() + appConfig['sessionTimeout']);
    //this.log.debug('Session Expire Time : ' + sessionTime.getHours() + ":" + sessionTime.getMinutes() + ":" + sessionTime.getSeconds());
    window.sessionStorage.setItem('sessionTimeout', sessionTime.getTime().toString());
  }

  public refreshSession() {
    /*
     * Session Time
     */
    let sessionTime = new Date();
    sessionTime.setMinutes(sessionTime.getMinutes() + appConfig['sessionTimeout']);
    //this.log.debug('Session Expire Time : ' + sessionTime.getHours() + ":" + sessionTime.getMinutes() + ":" + sessionTime.getSeconds());
    window.sessionStorage.setItem('sessionTimeout', sessionTime.getTime().toString());
  }


  public saveCfUserInfo(cf_user_guid: string, cf_user_id: string, cf_user_name: string, cf_given_name: string, cf_family_name: string,
                        cf_user_email: string, cf_phone_number: string, cf_expires: string, cf_previous_logon_time: string) {
    let now = new Date();
    /*
     * 필수 정보
     */
    window.sessionStorage.setItem('cf_user_guid', cf_user_guid);
    window.sessionStorage.setItem('cf_user_id', cf_user_id);
    //window.sessionStorage.setItem('user_name', cf_user_id);
    /*
     * 옵션 정보
     */
    if (cf_user_name == null) {
      cf_user_name = '';
    }
    window.sessionStorage.setItem('cf_username', cf_user_name);
    window.sessionStorage.setItem('user_name', cf_user_name);
    if (cf_given_name == null) {
      cf_given_name = '';
    }
    window.sessionStorage.setItem('cf_given_name', cf_given_name);
    if (cf_family_name == null) {
      cf_family_name = '';
    }
    window.sessionStorage.setItem('cf_family_name', cf_family_name);
    if (cf_user_email == null) {
      cf_user_email = '';
    }
    window.sessionStorage.setItem('cf_user_email', cf_user_email);
    if (cf_phone_number == null) {
      cf_phone_number = '';
    }
    window.sessionStorage.setItem('tell_phone', cf_phone_number);
    if (cf_previous_logon_time == null) {
      cf_previous_logon_time = '';
    }

    if (cf_user_email == null) {
      cf_user_email = '';
    }
    window.sessionStorage.setItem('address', cf_user_email);
    window.sessionStorage.setItem('cf_previous_logon_time', cf_previous_logon_time);

  }

  public saveUserInfo(user_id: string, user_name: string, status: string, tell_phone: string, zip_code: string,
                      address: string, admin_yn: string, img_path: string) {

    /*
     * 필수 정보
     */
    //window.sessionStorage.setItem('cf_user_id', user_id);
    window.sessionStorage.setItem('status', status);
    window.sessionStorage.setItem('admin_yn', admin_yn);


    /*
     * 옵션 정보
     */
    if (user_name == null) {
      user_name = '';
    }
    //window.sessionStorage.setItem('user_name', user_name);
    if (tell_phone == null) {
      tell_phone = '';
    }
    // window.sessionStorage.setItem('tell_phone', tell_phone);
    // if (zip_code == null) {
    //   zip_code = '';
    // }
    // window.sessionStorage.setItem('zip_code', zip_code);
    // if (address == null) {
    //   address = '';
    // }
    // window.sessionStorage.setItem('address', address);
    // if (img_path == null) {
    //   img_path = '';
    // }
    try {
      var pathHeader = img_path.lastIndexOf("/");
      var pathEnd = img_path.length;
      var fileName = img_path.substring(pathHeader + 1, pathEnd);
      this.doStorageGet('/storageapi/v2/swift/' + fileName, this.getToken()).subscribe(data => {
        let reader = new FileReader();
        reader.addEventListener("load", () => {
          window.sessionStorage.setItem('img_path', reader.result);
        }, false);
        if (data) {
          reader.readAsDataURL(data);
        }
      }, error => {
      });
    } catch (e) {
    }
  }

  public getSessionTime(): string {
    return sessionStorage.getItem('sessionTimeout');
  }

  public getUserGuid(): string {
    return sessionStorage.getItem('cf_user_guid');
  }

  public getUserid(): string {
    return sessionStorage.getItem('cf_user_id');
  }

  public getUserName(): string {
    return sessionStorage.getItem('cf_user_id');
  }

  public getUserEmail(): string {
    return sessionStorage.getItem('cf_user_email');
  }

  public getTokenType(): string {
    return sessionStorage.getItem('cf_token_type');
  }

  public getCfUserName(): string {
    return sessionStorage.getItem('cf_username');
  }

  public getTellphone(): string{
    return sessionStorage.getItem('tell_phone');
  }

  public getToken(): string {

    const cf_expires = sessionStorage.getItem('expire_date');
    const cf_token = this.getTokenWithoutRefresh();

    let now = new Date();
    if (cf_token != null && cf_expires <= now.getTime().toString()) {
        this.signOn(false);
    } else if (cf_token == null) {
      return null;
    }
    return this.getTokenWithoutRefresh();
  }

  private getTokenWithoutRefresh(): string {
    return sessionStorage.getItem('cf_token');
  }

  public getRefreshToken(): string {
    return sessionStorage.getItem('cf_refresh_token');
  }

  public getExpiresin(): string {
    return sessionStorage.getItem('expires_in');
  }

  public getScope(): string {
    return sessionStorage.getItem('cf_scope');
  }

  public getLoginType(): string {
    return sessionStorage.getItem('login_type');
  }

  public getImagePath(): string {
    try {
      let imgPath: string = sessionStorage.getItem('img_path');
      if (isNullOrUndefined(imgPath)) {
        return '/assets/resources/images/account/profile-thumbnail-sample.png';
      }
      let index = imgPath.indexOf('data:application/json;base64');
      if (isNullOrUndefined(imgPath) || imgPath.trim() === "" || index !== -1) {
        imgPath = '/assets/resources/images/account/profile-thumbnail-sample.png';
        this.log.trace('Header img path is empty string. So, image url set default.');
      }
      return imgPath;
    } catch (e) {
      return '/assets/resources/images/account/profile-thumbnail-sample.png';
    }
  }


  private map: Param;


  setUrl(url: string) {

    if (url.indexOf('?') < 0) {
      return url;
    }
    let paramStr = url.split('?');
    return paramStr[0];
  }

  setParams(url: string) {
    this.map = {};
    if (url.indexOf('?') < 0) {
      return this.map;
    }
    let paramStr = url.split('?')
    if (paramStr.length > 0) {
      paramStr = paramStr[1].split('&');
      paramStr.forEach(value => {
        let data = value.split('=');
        if (data.length > 0) {
          this.map[data[0].toString()] = data[1];
        }
      });
    }
    return this.map;
  }


  /*
   * 토큰은 재생성 하는 과정 - > OAUTH 로그인용
   */
  doTokenRefreshOauth() {
    this.signOut();
  }


  /*
   * 토큰은 재생성 하는 과정 - > OAUTH 로그인용
   */
  doTokenRefreshAPI() {

    let param = {'token': sessionStorage.getItem('cf_token'), 'refresh_token': this.getRefreshToken()};
    return this.doPost('/portalapi/token/refresh', param, sessionStorage.getItem('cf_token')).retryWhen(error => {
      return error.flatMap((error: any) => {
        return Observable.of(error.status).delay(1000);
      }).take(3).concat(Observable.throw({error: 'Sorry, there was an error (after 3 retries)'}));
    }).subscribe(data => {
      this.saveToken(data['token_type'], data['access_token'], data['refresh_token'], data['expires_in'], data['scope'], 'API');
      return true;
    }, error => {
      this.removeItems();
      return false;
    });
  }

  reloadPage() {
    const currentLocation = window.location.pathname;

    //this.log.debug("Reload page :", currentLocation);
    this.router.navigate(['/login'], {queryParams: {returnUrl: currentLocation}});
  }

  setCurrentAppName(value: any) {
    window.sessionStorage.setItem('_currentAppName', value);
  }

  setCurrentAppGuid(value: any) {
    window.sessionStorage.setItem('_currentAppGuid', value);
  }

  setCurrentLocation(value: any) {
    window.sessionStorage.setItem('_currentLocation', value);
  }

  setCurrentOrgName(value: any) {
    window.sessionStorage.setItem('_currentOrgName', value);
  }

  setCurrentOrgGuid(value: any) {
    window.sessionStorage.setItem('_currentOrgGuid', value);
  }

  setCurrentSpaceName(value: any) {
    window.sessionStorage.setItem('_currentSpaceName', value);
  }

  setCurrentSpaceGuid(value: any) {
    window.sessionStorage.setItem('_currentSpaceGuid', value);
  }

  setCurrentCatalogNumber(value: any) {
    window.sessionStorage.setItem('catalog_number', value);
  }

  setImagePath(value: any) {
    window.sessionStorage.setItem('img_path', value);
  }

  setUserName(value: any) {
    return sessionStorage.setItem('user_name', value);
  }


  getCurrentAppName(): any {
    return window.sessionStorage.getItem('_currentAppName');
  }

  getCurrentAppGuid(): any {
    return window.sessionStorage.getItem('_currentAppGuid');
  }

  getCurrentLocation(): any {
    return window.sessionStorage.getItem('_currentLocation');
  }

  getCurrentOrgName(): any {
    return window.sessionStorage.getItem('_currentOrgName');
  }

  getCurrentOrgGuid(): any {
    return window.sessionStorage.getItem('_currentOrgGuid');
  }

  getCurrentSpaceName(): any {
    return window.sessionStorage.getItem('_currentSpaceName');
  }

  getCurrentSpaceGuid(): any {
    return window.sessionStorage.getItem('_currentSpaceGuid');
  }

  getCurrentCatalogNumber(): any {
    return window.sessionStorage.getItem('catalog_number');
  }

  getMonitoring(): boolean {
    return appConfig['monitoring'];
  }

  getQuantity(): boolean {
    return appConfig['quantity'];
  }

  getAutomaticApproval(): boolean {
    return !appConfig['automaticApproval'];
  }

  alertMessage(value, result) {
    $(".alertLayer .in").html(value);
    if (result) {
      $(".alertLayer").css('border-left', '4px solid #3d10ef');
    } else {
      $(".alertLayer").css('border-left', '4px solid #cb3d4a');
    }
    $(".alertLayer").addClass("moveAlert");
    setTimeout(() => $(".alertLayer").removeClass("moveAlert"), 3000);
  }
}
