package com.project.controller.portal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.model.dto.PriceSearchParam;
import com.project.model.dto.RegUser;
import com.project.mybatis.domain.PriceSolution;
import com.project.service.portal.PriceSearchService;
import com.project.service.portal.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangyinqing
 */
@Controller
public class LoginController {

    UserService userService;
    PriceSearchService priceSearchService;

    @ResponseBody
    @RequestMapping("/login")
    public String login(@RequestParam() String account, @RequestParam() String password, HttpServletResponse response) throws Exception {
        RegUser regUser = userService.findUserByAccountAndPass(account, password);
        if (regUser == null) {
            regUser = userService.findUserByMobileAndPass(account, password);
        }
        Map map = new HashMap();
        if (regUser == null) {
            map.put("code", 0);
            map.put("message", "账号或者密码不正确");
        } else {
            String token = userService.generateSut(regUser);
            Cookie cookie = new Cookie("sut", token);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
            map.put("code", 1);
            map.put("user", regUser);
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(map);
    }

    @RequestMapping("validateUser")
    @ResponseBody
    public String vaildate(@CookieValue(value = "sut", required = false, defaultValue = "") String token) throws Exception {
        RegUser user = userService.getUserByToken(token);
        Map map = new HashMap();
        if (user == null) {
            map.put("code", 0);
        } else {
            map.put("code", 1);
            map.put("user", user);
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(map);
    }

    @RequestMapping("invalidateUser")
    @ResponseBody
    public String invaildate(@CookieValue("sut") String token, HttpServletResponse response) throws Exception {
        RegUser user = userService.removeUserByToken(token);
        Map map = new HashMap();
        if (user == null) {
            map.put("code", 0);
        } else {
            map.put("code", 1);
            map.put("user", user);
            Cookie cookie = new Cookie("sut", null);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(map);
    }

    @RequestMapping("searchPriceSolution")
    @ResponseBody
    public String searchPriceSolution(@RequestBody PriceSearchParam priceSearchParam) throws Exception {
        List<PriceSolution> priceSolutionList = priceSearchService.searchPriceSolution(priceSearchParam);
        Map map = new HashMap();
        map.put("priceSolutionList", priceSearchParam);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(map);
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setPriceSearchService(PriceSearchService priceSearchService) {
        this.priceSearchService = priceSearchService;
    }
}
