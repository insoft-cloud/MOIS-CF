package com.infranics.demo.controller;

import com.infranics.demo.service.StorageService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.json.JSONArray;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class StorageController {

    final StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/storage")
    public ModelAndView webStorage(){
        ModelAndView mv = new ModelAndView("storage");
        return mv;
    }

    @PostMapping("/storage")
    public Map storage_Login(@RequestBody Map storage_info, HttpServletRequest request){
        return storageService.storage_Login(storage_info,request);
    }

    @GetMapping("/storage/containers")
    public Map storage_List(HttpServletRequest request){
        return storageService.storage_List(request);
    }

    @PostMapping("/storage/container")
    public Map storage_Create(HttpServletRequest request,@RequestBody Map container){
        return storageService.storage_Create(container,request);
    }

    @DeleteMapping("/storage/container/{container_name}")
    public Map storage_Delete(@PathVariable String container_name,HttpServletRequest request){
        return storageService.storage_Delete(container_name,request);
    }

    @GetMapping("/storage/container/{container_name}/objects")
    public Map object_List(@PathVariable String container_name, HttpServletRequest request){
        return storageService.object_List(container_name,request);
    }

    @PutMapping("/storage/container/object")
    public Map object_summary(@RequestBody Map object_name,HttpServletRequest request){
        return storageService.object_summary(object_name.get("name").toString(),request);
    }

    @PostMapping("/storage/container/object")
    public Map object_Upload(@RequestParam("file") MultipartFile multipartFile,HttpServletRequest request){
        return storageService.object_Upload(multipartFile,request);
    }

    @DeleteMapping("/storage/container/object")
    public Map object_Delete(@RequestBody Map object_name, HttpServletRequest request){
        return storageService.object_Delete(object_name.get("name").toString(),request);
    }

    @GetMapping("/download/url")
    public void download(@PathParam("name") String name, HttpServletRequest request, HttpServletResponse response) throws Exception {
        storageService.download(name,request,response);
    }



//    @PostMapping("/storage/container/object/download")
//    public Map object_Download(@RequestBody Map object_name, HttpServletRequest request){
//        return storageService.object_Download(object_name.get("name").toString(),request);
//    }


}
