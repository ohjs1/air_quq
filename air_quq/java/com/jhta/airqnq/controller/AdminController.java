package com.jhta.airqnq.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jhta.airqnq.pageUtil.PageUtil;
import com.jhta.airqnq.service.MemberService;
import com.jhta.airqnq.vo.JoinVo;

@Controller
public class AdminController {
	
	@Autowired
	private MemberService service;
	
	@GetMapping("/admin")
	public String admin() {
		return ".admin";
	}
	
	@GetMapping("/admin/member/list")
	public String memberlist(Model model, @RequestParam(value="pageNum",defaultValue = "1")int pageNum,String field,String keyword) {

		HashMap<String, Object> map=new HashMap<String, Object>();
		
		map.put("keyword", keyword);
		map.put("field",field);
		
		int totalRowCount=service.MemberCnt(map);
		
		PageUtil pu=new PageUtil(pageNum, totalRowCount, 10, 2);
		
		map.put("startRow",pu.getStartRow());
		map.put("rowblockcount",pu.getRowBlockCount());
		
		List<JoinVo> Memberlist = service.Memberlist(map);
		
		model.addAttribute("pu", pu);
		model.addAttribute("Memberlist", Memberlist);
		model.addAttribute("keyword", keyword);
		model.addAttribute("field", field);
		return ".admin.memberadmin";
	}
	
	@RequestMapping(value="/json/memberlistfind", produces ="application/json;charset=utf-8")
	@ResponseBody
	public HashMap<String, Object> memberlistfind(@RequestParam(value="pageNum",defaultValue = "1")int pageNum,String field,String keyword) {

		HashMap<String, Object> map=new HashMap<String, Object>();
		HashMap<String, Object> returndata=new HashMap<String, Object>();
		
		map.put("keyword", keyword);
		map.put("field",field);
		
		int totalRowCount=service.MemberCnt(map);
		
		PageUtil pu=new PageUtil(pageNum, totalRowCount, 10, 2);
		
		map.put("startRow",pu.getStartRow());
		map.put("rowblockcount",pu.getRowBlockCount());
		
		List<JoinVo> Memberlist = service.Memberlist(map);
		
		returndata.put("pu", pu);
		returndata.put("Memberlist", Memberlist);
		returndata.put("keyword", keyword);
		returndata.put("field", field);
		return returndata;
	}
	
	@GetMapping(value="/json/memberone", produces ="application/json;charset=utf-8")
	@ResponseBody
	public JoinVo memberone(int menum) {
		JoinVo vo = service.MemberOne(menum);
		return vo;
	}
	
	@GetMapping("/memberimg")
	public void memberimg(int menum, HttpServletResponse resp) {
		resp.setContentType("image/jpeg");

		JoinVo vo = service.MemberOne(menum);
		
		ByteArrayInputStream data =new ByteArrayInputStream(vo.getProfile_img());
		try {
			ServletOutputStream os=resp.getOutputStream();
			
			int dataReader=0;
			
			while((dataReader=data.read()) != -1) {
				os.write(dataReader);
			}
		} catch (IOException ie) {
			System.out.println(ie.getMessage());
		}
	}
	
	@GetMapping("/admin/memberimgreset")
	public String memberupdate(int menum) {
		
		int n=service.MemberImgReset(menum);
		
		if(n>0) {
			return "redirect:/admin/member/list";
		}else {
			return ".error";
		}
	}
}
