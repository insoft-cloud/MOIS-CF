package com.infranics.demo.service;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.javaswift.joss.client.factory.AccountConfig;
import org.javaswift.joss.client.factory.AccountFactory;
import org.javaswift.joss.client.factory.AuthenticationMethod;
import org.javaswift.joss.client.factory.AuthenticationMethodScope;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.model.StoredObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StorageService {


    public Map storage_Login(Map storage_info, HttpServletRequest request){
        AccountConfig config = new AccountConfig();
        config.setPassword(storage_info.get("password").toString());
        config.setAuthUrl(storage_info.get("authUrl").toString());
        config.setUsername(storage_info.get("username").toString());
        config.setAuthenticationMethodScope(AuthenticationMethodScope.PROJECT_NAME);
        config.setTenantName("admin");
        config.setAuthenticationMethod(AuthenticationMethod.KEYSTONE_V3);
        Account account = new AccountFactory(config).createAccount();
        Map map = new HashMap();
        map.put("result", "true");
        request.getSession().setAttribute("account", account);
        return map;
    }

    public Map storage_List(HttpServletRequest request){
        Account account = getAccount(request);
        Map map = new HashMap();
        map.put("result", account.list().stream().map(r ->  r.getName()).collect(Collectors.toList()));
        return map;
    }

    public Map storage_Create(Map name, HttpServletRequest request){
        Map map = new HashMap();
        try {
            Account account = getAccount(request);
            account.getContainer(name.get("name").toString()).create().makePublic();
            map.put("result", true);
        }catch (Exception e){
            map.put("result", false);
            map.put("message", e.getMessage());
        }

        return map;
    }

    public Map storage_Delete(String name, HttpServletRequest request){
        Map map = new HashMap();
        try {
            Account account = getAccount(request);
            account.getContainer(name).delete();
            map.put("result", true);
        } catch (Exception e){
            map.put("result", false);
            map.put("message", e.getMessage());
        }
        return map;
    }

    public Map object_List(String container_name, HttpServletRequest request){
        Account account = getAccount(request);
        Container container = account.getContainer(container_name);
        request.getSession().setAttribute("container", container_name);
        Map map = new HashMap();
        map.put("result", container.list().stream().map(r ->  r.getName()).collect(Collectors.toList()));
        return map;
    }

    public Map object_summary(String object_name , HttpServletRequest request){
        StoredObject storedObject = getContainer(request).getObject(object_name);
        long store = storedObject.getContentLength();
        String size = "";
        if(store > 1024L){
            store = store/1024;
            size = "KB";
        }
        if(store > 1024L){
            store = store/1024;
            size = "MB";
        }
        if(store > 1024L){
            store = store/1024;
            size = "GB";
        }
        Map map = new HashMap();
        map.put("name",storedObject.getName());
        map.put("public_url", storedObject.getPublicURL());
        map.put("type", storedObject.getContentType());
        map.put("length", (store + size));
        return map;
    }

    public Map object_Upload(MultipartFile file, HttpServletRequest request){
        Map map = new HashMap();
        try {
            StoredObject object = getContainer(request).getObject(file.getOriginalFilename());
            File R_file = new File(file.getOriginalFilename());
            R_file.createNewFile();
            FileOutputStream fos = new FileOutputStream(R_file);
            fos.write(file.getBytes());
            fos.close();
            object.uploadObject(R_file);
            R_file.delete();
            map.put("result", getContainer(request).list().stream().map(r ->  r.getName()).collect(Collectors.toList()));
        } catch (Exception e){
            map.put("result", false);
            map.put("message", e.getMessage());
        }
        return map;
    }

    public Map object_Delete(String file_name, HttpServletRequest request){
        Map map = new HashMap();
        StoredObject object = getContainer(request).getObject(file_name);
        object.delete();
        return map;
    }

//    public Map storage_Download(String name,MultipartFile file, HttpServletRequest request){
//        Map map = new HashMap();
//        try {
//            Account account = getAccount(request);
//            Container container = account.getContainer(name);
//            StoredObject object = container.getObject(file.getOriginalFilename());
//            File R_file = new File(file.getOriginalFilename());
//            R_file.createNewFile();
//            FileOutputStream fos = new FileOutputStream(R_file);
//            fos.write(file.getBytes());
//            fos.close();
//            object.uploadObject(R_file);
//            R_file.delete();
//            map.put("result", true);
//        } catch (Exception e){
//            map.put("result", false);
//            map.put("message", e.getMessage());
//        }
//        return map;
//    }

    public void download(String name, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // MIME Type 을 application/octet-stream 타입으로 변경
        // 무조건 팝업(다운로드창)이 뜨게 된다.
        response.setContentType("application/octet-stream");

        // 브라우저 별로 파일명 인코딩 방식이 다르기 때문에 브라우저 별로 파일이름을 가져온다.
        String fileNameForBrowser = getDisposition(name, getBrowser(request));

        StoredObject storedObject = getContainer(request).getObject(name);
        // 파일명 지정
        response.setHeader("Content-Disposition", "attachment; filename="+name);

        OutputStream os = response.getOutputStream();
        InputStream is = new URL(storedObject.getPublicURL()).openStream();

        // the new way
        final int returnCode = IOUtils.copy( is, os );
        if (returnCode == -1)
            IOUtils.copyLarge( is, os );

        IOUtils.closeQuietly( is );
        IOUtils.closeQuietly( os );
    }

    private String getBrowser(HttpServletRequest request) {
        String header = request.getHeader("User-Agent");
        if (header.indexOf("MSIE") > -1) {
            return "MSIE";
        } else if (header.indexOf("Chrome") > -1) {
            return "Chrome";
        } else if (header.indexOf("Opera") > -1) {
            return "Opera";
        } else if (header.indexOf("Trident/7.0") > -1){
            //IE 11 이상 //IE 버전 별 체크 >> Trident/6.0(IE 10) , Trident/5.0(IE 9) , Trident/4.0(IE 8)
            return "MSIE";
        }

        return "Firefox";
    }

    private String getDisposition(String filename, String browser) throws Exception {
        String encodedFilename = null;

        if (browser.equals("MSIE")) {
            encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
        } else if (browser.equals("Firefox")) {
            encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
        } else if (browser.equals("Opera")) {
            encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
        } else if (browser.equals("Chrome")) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < filename.length(); i++) {
                char c = filename.charAt(i);
                if (c > '~') {
                    sb.append(URLEncoder.encode("" + c, "UTF-8"));
                } else {
                    sb.append(c);
                }
            }
            encodedFilename = sb.toString();
        } else {
            throw new RuntimeException("Not supported browser");
        }

        return encodedFilename;
    }



    private Account getAccount(HttpServletRequest request){
        return (Account) request.getSession().getAttribute("account");
    }

    private Container getContainer(HttpServletRequest request){
        return getAccount(request).getContainer(request.getSession().getAttribute("container").toString());
    }

}
