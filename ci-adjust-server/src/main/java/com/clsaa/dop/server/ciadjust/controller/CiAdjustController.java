package com.clsaa.dop.server.ciadjust.controller;

import org.springframework.web.bind.annotation.*;
import java.io.InputStream;
import java.io.DataInputStream;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import com.clsaa.dop.server.ciadjust.dao.*;

@RestController
@RequestMapping("/spiderData")
public class CiAdjustController {

    @RequestMapping(value = "/dataProcess", method = RequestMethod.GET, headers = "Accept=application/json")
    public void dataProcess() {
        try {
            String filePath1 = "D:\\software\\python\\projects\\gitlabspider\\MyGitlabScrapy\\data\\commit_info.json";
            String filePath2 = "D:\\software\\python\\projects\\gitlabspider\\MyGitlabScrapy\\data\\project_commit.json";

            System.out.println("start");
            String[] args1=new String[]{"python","D:\\software\\python\\projects\\gitlabspider\\MyGitlabScrapy\\dataProcess\\commitDataIntoNum.py",filePath1,filePath2};
            Process pr=Runtime.getRuntime().exec(args1);
            BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            int re = pr.waitFor();
            System.out.println(re);
            System.out.println("end");

            new Thread(new SyncPipe(pr.getErrorStream(), System.err)).start();
            new Thread(new SyncPipe(pr.getInputStream(), System.out)).start();
            PrintWriter stdin = new PrintWriter(pr.getOutputStream());
            stdin.println("python "+"D:\\software\\python\\projects\\gitlabspider\\MyGitlabScrapy\\dataProcess\\commitDataIntoNum.py "+"D:\\software\\python\\projects\\gitlabspider\\MyGitlabScrapy\\data\\commit_info.json "+"D:\\software\\python\\projects\\gitlabspider\\MyGitlabScrapy\\data\\project_commit.json");//此处自行填写，切记有空格，跟cmd的执行语句一致。
            stdin.close();

            }
            catch (Exception e) {
                e.printStackTrace();
            }

         }

}