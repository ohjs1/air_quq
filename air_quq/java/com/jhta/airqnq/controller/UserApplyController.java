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
import org.springframework.web.client.RestTemplate;

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
	public String userapply(Model model,HttpSession session, String stary_day, String end_day, int hinum, int people_count) {
		
		Apply_infoVo infovo= house_infoService.HinumSelect(hinum);
		HashMap<String, String> usercheck=new HashMap<String, String>();
		
		String start=stary_day.replace("-", "/");
		String end=end_day.replace("-", "/");
		
		usercheck.put("checkIn", start);
		usercheck.put("checkOut", end);
		usercheck.put("max_n", people_count+"");
		
		try {
			
			SimpleDateFormat format=new SimpleDateFormat("yyyy/MM/dd");
			
			Date start2=(Date) format.parse(start);
			Date end2=(Date) format.parse(end);
			
			java.sql.Date rentstart=new java.sql.Date(start2.getTime());
			java.sql.Date rentend=new java.sql.Date(end2.getTime());
			
			long timebetween1=end2.getTime()-start2.getTime();
			
			long timebetween2=timebetween1/(24*60*60*1000);
			
			long timebetween3=timebetween2*infovo.getPrice();
			
			ApplyVo vo=new ApplyVo(start, end, people_count, (int)timebetween3);
			RentVo rentvo=new RentVo(0, hinum, 0, rentstart, rentend, 1, people_count, (int)timebetween3, "n", null, null,1);
			
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

//	@RequestMapping("/refund/access_token")
//	@ResponseBody
//	public HashMap<String, Object> updateSpittle(String imp_key,String imp_secret) {
//	    
//		HashMap<String, Object> request = new HashMap<String, Object>();
//		request.put("imp_key", imp_key);
//		request.put("imp_secret",imp_secret);
//		
//		RestTemplate rest = new RestTemplate();
//		System.out.println(rest.postForObject("https://api.iamport.kr/users/getToken", request,request.getClass()));
//		return rest.postForObject("https://api.iamport.kr/users/getToken", request,request.getClass());
//	}
//	
//	@RequestMapping("/refund/use")
//	@ResponseBody
//	public HashMap<String, Object> refunttest(String imp_uid,String reason,int amount,String accountname,String banknumber,String accountnumber,String Authorization) {
//		
//		RestTemplate rest = new RestTemplate();
//		
//		HashMap<String, Object> postrequest=new HashMap<String, Object>();
//		
//		postrequest.put("Content-Type", "application/json");
//		postrequest.put("access_token", Authorization);
//		postrequest.put("imp_uid", imp_uid);
//		postrequest.put("amount", amount);
//		if(accountname != null) {
//			postrequest.put("refund_holder",accountname);
//			postrequest.put("refund_bank",banknumber);
//			postrequest.put("refund_account",accountnumber);
//		}
//		
//		rest.postForObject("https://api.iamport.kr/payments/cancel", postrequest, postrequest.getClass());
//		return rest.postForObject("https://api.iamport.kr/payments/cancel", postrequest, postrequest.getClass());
//	}
	
	/* 준범 */
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
		if ((boolean) session.getAttribute("logind")) {
			return ".apply.applyCheck";
		} else {
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
