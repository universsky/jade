/**
 * 
 */
package com.ofpay.edge.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author jack
 *
 */
@Controller
public class UploadFileController {
	private static final Logger logger = LoggerFactory.getLogger(UploadFileController.class);

	@RequestMapping(value = "/upload")
	@ResponseBody
	public String upload(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request,
			ModelMap model) {
		String fileName = file.getOriginalFilename();
		logger.info("开始上传：{}", fileName);

		String path = request.getSession().getServletContext().getRealPath("/WEB-INF/third_lib");
		logger.info("path：{}", path);

		File targetFile = new File(path, fileName);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}

		// 保存
		try {
			file.transferTo(targetFile);
		} catch (Exception e) {
			e.printStackTrace();
			return "Upload failed!";
		}
		return "Upload success! File server path:" + targetFile;

	}

}
