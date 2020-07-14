package com.jhta.airqnq.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jhta.airqnq.service.HostService;
import com.jhta.airqnq.vo.HouseInfoVo;

@Controller
public class HostController {
	
	@Autowired
	private HostService service;
	
	@GetMapping(value="/host/epOnlineList")
	public String epOnlineList(Model model, HttpSession session) {
		//int menum = (int)session.getAttribute("menum");
		int menum = 1000;
		List<HouseInfoVo> list = service.list(menum);
		model.addAttribute("list", list);
		return ".host.epOnlineList";
	}
	
	@RequestMapping(value = "/host/regist/1")
	public String hostRegist1(String addr, String roomStatus, String clientCount, HttpSession session, int next) {
		System.out.println("주소" + addr + ", 집상태:" + roomStatus + ", 숙박인원" + clientCount + "다음단계:" + next);
		session.setAttribute("hostStep", next);
		return ".hostRegist";
	}
	@RequestMapping(value = "/host/regist/2")
	public String hostRegist2(String hostTitle, String hostContent, HttpSession session, int next) {
		System.out.println("제목:" + hostTitle + ", 내용:" + hostContent + "다음단계:" + next);
		session.setAttribute("hostStep", next);
		return ".hostRegist";
	}
}
