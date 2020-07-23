package com.jhta.airqnq.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jhta.airqnq.service.ApplyService;
import com.jhta.airqnq.service.House_infoAdminService;
import com.jhta.airqnq.service.MemberService;
import com.jhta.airqnq.vo.ApplyVo;
import com.jhta.airqnq.vo.Apply_infoVo;
import com.jhta.airqnq.vo.JoinVo;
import com.jhta.airqnq.vo.MemberVo;
import com.jhta.airqnq.vo.RentVo;

@Controller
public class UserApplyController {

	@Autowired
	private ApplyService service;
	
	@Autowired
	private MemberService memberservice;

	@Autowired
	private House_infoAdminService house_infoService;

	@RequestMapping(value="/user/apply")
	public String userapply(Model model,HttpSession session, String stary_day, String end_day, int price, int people_count) {
		
		System.out.println("넘어온 데이터:" + stary_day + ", " + end_day + ", " + price + ", " + people_count);
		
		int hinum=5;
		Apply_infoVo infovo= house_infoService.HinumSelect(hinum);
		HashMap<String, String> usercheck=new HashMap<String, String>();
		
		String start="2020/07/22";
		String end="2020/07/24";
		int memberCNT=5;

		usercheck.put("checkIn", start);
		usercheck.put("checkOut", end);
		usercheck.put("max_n", memberCNT+"");
		
		try {
			
			SimpleDateFormat format=new SimpleDateFormat("yyyy/MM/dd");
			
			Date start2=(Date) format.parse(start);
			Date end2=(Date) format.parse(end);
			
			java.sql.Date rentstart=new java.sql.Date(start2.getTime());
			java.sql.Date rentend=new java.sql.Date(end2.getTime());
			
			long timebetween1=end2.getTime()-start2.getTime();
			
			long timebetween2=timebetween1/(24*60*60*1000);
			
			long timebetween3=timebetween2*infovo.getPrice();
			
			ApplyVo vo=new ApplyVo(start, end, memberCNT, (int)timebetween3);
			RentVo rentvo=new RentVo(0, hinum, infovo.getMenum(), rentstart, rentend, 1, 0, (int)timebetween3, "n", null, null);
			
			session.setAttribute("applyVo", vo);
			session.setAttribute("rentVo", rentvo);
			
		} catch (ParseException pe) {
			System.out.println("날짜 오류 :"+pe);
		}
		
		model.addAttribute("infovo", infovo);
		model.addAttribute("usercheck", usercheck);
		return ".apply.userapply";
	}
	
	@GetMapping("/user/apply/memberimg")
	public void memberimg(int menum, HttpServletResponse resp) {
		resp.setContentType("image/jpeg");

		try {
			JoinVo vo = memberservice.MemberOne(menum);
	
			ByteArrayInputStream data =new ByteArrayInputStream(vo.getProfile_img());
		
			ServletOutputStream os=resp.getOutputStream();
			
			int dataReader=0;
			
			while((dataReader=data.read()) != -1) {
				os.write(dataReader);
			}
		} catch (IOException ie) {
			System.out.println(ie.getMessage());
		} catch (NullPointerException np) {}
	}

	@PostMapping(value = "/user/apply/setApply")
	@ResponseBody
	public void totmoney(HttpSession session, ApplyVo vo) {
		RentVo rent=(RentVo)session.getAttribute("rentVo");
		
		try {
			String start=vo.getCheckIn();
			String end=vo.getCheckIn();
			
			SimpleDateFormat format=new SimpleDateFormat("yyyy/MM/dd");
			
			Date start2=(Date) format.parse(start);
			Date end2=(Date) format.parse(end);
			
			java.sql.Date rentstart=new java.sql.Date(start2.getTime());
			java.sql.Date rentend=new java.sql.Date(end2.getTime());
			
			rent.setStartrent(rentstart);
			rent.setEndrent(rentend);
			rent.setPerson(vo.getMax_n());
			
			session.setAttribute("rentVo", rent);
			session.setAttribute("applyVo", vo);
			
		} catch (ParseException pe) {
			System.out.println("수정 날짜 오류"+pe.getMessage());
		}
	}

	@GetMapping(value = "/user/applyCheck")
	public String apply(HttpSession session) {
		MemberVo vo=(MemberVo) session.getAttribute("memberVo");
		
		if(vo != null) {
			return ".apply.applyCheck";
		}else {
			return ".login";
		}
	}

	@GetMapping("/user/applyCheck/getMemberInfo")
	@ResponseBody
	public MemberVo getMemberInfo(HttpSession session) {
		return (MemberVo) session.getAttribute("memberVo");
	}
	
	@PostMapping("/user/apply/cencel")
	@ResponseBody
	public String applyCencel(HttpSession session, int rtnum) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("menum", session.getAttribute("menum"));
		map.put("rtnum", rtnum);
		try {
			service.applyCencel(map);
			return "success";
		}catch (Exception e) {
			System.out.println(e.getMessage());
			return "error";
		}
	}
}
