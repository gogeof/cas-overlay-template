package org.example.gogeof.random;

import org.apereo.cas.web.AbstractDelegateController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.OutputStream;
import java.util.Random;

public class RandomController extends AbstractDelegateController {
    @Override
    public boolean canHandle(HttpServletRequest request, HttpServletResponse response) {
        return true;
    }

    @GetMapping(value = RandomConstants.REQUEST_MAPPING, produces = "text/plain")
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //存储验证码到session
        HttpSession session = request.getSession();
        String randomNum;
        if (session.getAttribute("random_num") == null) {
            randomNum = getRandom(12);
            session.setAttribute("random_num", randomNum);
        }else{
            randomNum = (String)session.getAttribute("random_num");
        }

        //返回给用户
        //设置response头信息
        //禁止缓存
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/plain");

        OutputStream outputStream =  response.getOutputStream();
        outputStream.write(randomNum.getBytes());
        return null;
    }

    // 只允许生成长度在　1-32 的字符串
    private String getRandom(int strLength){
        Random rm = new Random();

        // 获得随机数
        double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);

        // 将获得的获得随机数转化为字符串
        String fixLenthString = String.valueOf(pross);

        // 返回固定的长度的随机数
        return fixLenthString.substring(2, strLength + 2);
    }
}
