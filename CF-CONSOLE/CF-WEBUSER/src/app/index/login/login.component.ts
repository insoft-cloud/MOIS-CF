import {Component, OnInit} from '@angular/core';
import {SecurityService} from "../../auth/security.service";
import {CommonService} from "../../common/common.service";
import {NGXLogger} from "ngx-logger";
import {ActivatedRoute, Router} from "@angular/router";
import {LoginService} from "./login.service";
import {isNullOrUndefined} from "util";
import {KeycloakService} from "keycloak-angular";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  errorMsg: string;
  returnUrl: string;
  public username: string;
  public password: string;
  key : boolean = true;


  constructor(public common: CommonService, private router: Router, private route: ActivatedRoute, private log: NGXLogger, private loginService: LoginService) {
    let egovp_user_name = this.route.snapshot.queryParams['USER_NAME'];
    let egovp_user_id = this.route.snapshot.queryParams['USER_ID'];
    let egovp_org_cd = this.route.snapshot.queryParams['ORG_CD'];
    let egovp_org_name = this.route.snapshot.queryParams['ORG_NAME'];
    let egovp_project_id = this.route.snapshot.queryParams['PROJECT_ID'];
    let egovp_project_name = this.route.snapshot.queryParams['PROJECT_NAME'];
    let egovp_user_se_cd = this.route.snapshot.queryParams['USER_SE_CD'];
    let returnUrl = this.route.snapshot.queryParams['returnUrl'] || 'dashboard';
    let no = this.route.snapshot.queryParams['no'] || 'dashboard';
    window.sessionStorage.setItem('loginCheck', "true");
    if(isNullOrUndefined(window.sessionStorage.getItem('egovp_user_name'))||
      isNullOrUndefined(window.sessionStorage.getItem('egovp_user_id'))||
      isNullOrUndefined(window.sessionStorage.getItem('egovp_org_cd'))||
      isNullOrUndefined(window.sessionStorage.getItem('egovp_org_name'))||
      isNullOrUndefined(window.sessionStorage.getItem('egovp_project_id'))||
      isNullOrUndefined(window.sessionStorage.getItem('egovp_project_name'))||
      isNullOrUndefined(window.sessionStorage.getItem('egovp_user_se_cd'))
    ) {
      if( isNullOrUndefined(egovp_user_name) || isNullOrUndefined(egovp_user_id) || isNullOrUndefined(egovp_org_cd)
        || isNullOrUndefined(egovp_org_name) || isNullOrUndefined(egovp_project_id) || isNullOrUndefined(egovp_project_name) || isNullOrUndefined(egovp_user_se_cd)){
        this.router.navigate(['/error']);
        return;
      }else {
        window.sessionStorage.setItem('egovp_user_name',decodeURI(atob(egovp_user_name.toString()).toString()).toString());
        window.sessionStorage.setItem('egovp_org_cd',atob(egovp_org_cd.toString()).toString());
        window.sessionStorage.setItem('egovp_org_name',atob(egovp_org_name.toString()).toString());
        window.sessionStorage.setItem('egovp_project_id',atob(egovp_project_id.toString()).toString());
        window.sessionStorage.setItem('egovp_project_name',atob(egovp_project_name.toString()).toString());
        window.sessionStorage.setItem('egovp_user_se_cd',atob(egovp_user_se_cd.toString()).toString());
        window.sessionStorage.setItem('returnUrl', returnUrl);
        window.sessionStorage.setItem('catalog_number', no);
      }
    }
    this.returnUrl = sessionStorage.getItem('returnUrl');
    //if (this.common.getToken() != null) {
    //  this.loginService.doGo(this.returnUrl);
    //  return ;
   // }
    this.common.signOn(true);
  }

  ngOnInit() {

  }


  apiLogin() {
    this.common.signOut();
    this.common.isLoading = true;
    this.loginService.apiLogin(this.username, this.password).subscribe(data => {
    }, error => {
      this.showMsg('');
      this.common.isLoading = false;
    });

  }


  oAuthLogin() {

    this.common.signOn(true);
    //this.loginService.oAuthLogin();
  }


  showMsg(msg: string) {
    this.common.signOut();
    this.common.isLoading = false;
    if (msg == '') {
      this.errorMsg = '사용자 계정 또는 비밀번호가 틀렸습니다.';
    }
    else if (msg == '1') {
      this.errorMsg = '로그인 과정에 문제가 발생하였습니다. 관리자에게 문의 하시길 바랍니다.';
    } else {
      this.errorMsg = msg;
    }

  }

  hideMsg() {

  }

}

export interface Param {
  [name: string]: any;
}
