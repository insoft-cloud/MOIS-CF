import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {isNullOrUndefined} from "util";
declare var $: any;
declare var jQuery: any;
@Component({
  selector: 'app-index',
  templateUrl: './index.component.html',
  styleUrls: ['./index.component.css']
})
export class IndexComponent implements OnInit, OnDestroy {

  constructor(private router: Router,  private activeRoute: ActivatedRoute, ) {
    let egovp_user_name = this.activeRoute.snapshot.queryParams['USER_NAME'];
    let egovp_user_id = this.activeRoute.snapshot.queryParams['USER_ID'];
    let egovp_org_cd = this.activeRoute.snapshot.queryParams['ORG_CD'];
    let egovp_org_name = this.activeRoute.snapshot.queryParams['ORG_NAME'];
    let egovp_project_id = this.activeRoute.snapshot.queryParams['PROJECT_ID'];
    let egovp_project_name = this.activeRoute.snapshot.queryParams['PROJECT_NAME'];
    let egovp_user_se_cd = this.activeRoute.snapshot.queryParams['USER_SE_CD'];
    let returnUrl = this.activeRoute.snapshot.queryParams['returnUrl'] || 'dashboard';
    let no = this.activeRoute.snapshot.queryParams['no'] || 'dashboard';
    if( isNullOrUndefined(egovp_user_name) || isNullOrUndefined(egovp_user_id) || isNullOrUndefined(egovp_org_cd)
      || isNullOrUndefined(egovp_org_name) || isNullOrUndefined(egovp_project_id) || isNullOrUndefined(egovp_project_name) || isNullOrUndefined(egovp_user_se_cd)){
      this.router.navigate(['/error']);
    }else {
      window.sessionStorage.removeItem('egovp_user_name');
      window.sessionStorage.removeItem('egovp_user_id');
      window.sessionStorage.removeItem('egovp_org_cd');
      window.sessionStorage.removeItem('egovp_org_name');
      window.sessionStorage.removeItem('egovp_project_id');
      window.sessionStorage.removeItem('egovp_project_name');
      window.sessionStorage.removeItem('egovp_user_se_cd');
      window.sessionStorage.removeItem('returnUrl');
      window.sessionStorage.removeItem('catalog_number');
      window.sessionStorage.setItem('egovp_user_name',egovp_user_name);
      window.sessionStorage.setItem('egovp_user_id',egovp_user_id);
      window.sessionStorage.setItem('egovp_org_cd',egovp_org_cd);
      window.sessionStorage.setItem('egovp_org_name',egovp_org_name);
      window.sessionStorage.setItem('egovp_project_id',egovp_project_id);
      window.sessionStorage.setItem('egovp_project_name',egovp_project_name);
      window.sessionStorage.setItem('egovp_user_se_cd',egovp_user_se_cd);
      window.sessionStorage.setItem('returnUrl',returnUrl);
      window.sessionStorage.setItem('catalog_number', no);
      this.router.navigate(['/login']);
    }
    //this.router.navigate(['/login']);
  }

  ngOnInit() {

    $(document).ready(function() {
      $('#fullpage').fullpage({
        scrollBar: true,

        navigation: true,
        navigationPosition: 'right',

        verticalCentered: true,
        css3:false,
      });

      var agent = navigator.userAgent.toLowerCase();

      if(agent.indexOf("chrome") != -1 || agent.indexOf("edge") != -1 || agent.indexOf("firefox") != -1) {
      } else {
        $("#layerpop_index_browser_notice").modal("show");
      }

    });

  }

  ngOnDestroy(){
    $('#fp-nav').remove();
  }

}
