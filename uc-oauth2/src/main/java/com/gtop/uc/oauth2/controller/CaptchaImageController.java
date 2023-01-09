package com.gtop.uc.oauth2.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.UUID;
import com.gtop.uc.common.cache.CacheService;
import com.gtop.uc.common.constant.AppConstant;
import com.gtop.uc.common.constant.GlobalProperties;
import com.gtop.uc.common.constant.SessionConstant;
import com.gtop.uc.common.response.ApiResponse;
import com.gtop.uc.oauth2.entity.CaptchaImage;
import com.gtop.uc.oauth2.entity.CaptchaImageMeta;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * 随记生成数字验证码
 *
 * @author hongzw
 */
@RestController
@RequestMapping(value = "/captchaImage")
public class CaptchaImageController {

	@Resource
	private CacheService cacheService;

	/**
	 * 生成认证码（后续校验使用缓存）
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/generate", method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public CaptchaImage generate(HttpServletResponse response) throws Exception {
		response.setContentType("image/jpeg");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		return getCaptchaImage();
	}

	/**
	 * 生成认证码（后续校验使用session）
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/generate2", method = {RequestMethod.GET,RequestMethod.POST})
	public void generate2(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("image/jpeg");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		getCaptchaImage2(request, response);
	}

	@RequestMapping(value = "/check", method = RequestMethod.POST)
	public ApiResponse<String> check(HttpServletRequest request) {
		String randomCode = request.getParameter("randomCode");
		Assert.hasText(randomCode, "必须传入randomCode！");

		String random = (String) request.getSession().getAttribute(SessionConstant.RANDOM_CODE_KEY);
		Assert.notNull(random, "验证码不存在！");

		if (random.equals(randomCode)) {
			return ApiResponse.successWithData("校验成功");
		} else {
			return ApiResponse.failedWithMsg(1108);
		}
	}

	/**
	 * 随机取得一个字体
	 * 
	 * @param
	 *
	 * @return Font 返回一个新字体
	 */
	private Font getsFont() {
		return new Font("Fixedsys", Font.CENTER_BASELINE, 22);
	}

	/**
	 * 返回一个随机颜色
	 * 
	 * @param
	 *            fc 随机数
	 * @param
	 *            bc 随机数
	 * @param
	 *            random 随机数
	 * @return Color 返回一个新颜色
	 */
	private Color getRandColor(int fc, int bc, Random random) {
		if (fc > 255) {
			fc = 255;
		}
		if (bc > 255) {
			bc = 255;
		}
		int r = fc + random.nextInt(bc - fc - 6);
		int g = fc + random.nextInt(bc - fc - 4);
		int b = fc + random.nextInt(bc - fc - 8);
		return new Color(r, g, b);
	}

	/**
	 * 生成随机数图片（返回图片流）
	 * @param request
	 * @param response
	 */
	private void getCaptchaImage2(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		CaptchaImageMeta meta = drawCaptchaImage();
		session.removeAttribute(SessionConstant.RANDOM_CODE_KEY);
		session.setAttribute(SessionConstant.RANDOM_CODE_KEY, meta.getImageNumber());

		OutputStream os = response.getOutputStream();
		ImageIO.write(meta.getImage(), "JPEG", os);
		os.close();
	}

	/**
	 * 生成随机数图片（返回图片base64编码字符串）
	 * @return
	 * @throws IOException
	 */
	private CaptchaImage getCaptchaImage() throws IOException {

		CaptchaImageMeta meta = drawCaptchaImage();

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ImageIO.write(meta.getImage(), "JPEG", stream);
		String base64Img = Base64.encode(stream.toByteArray());
		stream.flush();
		stream.close();
		String uuid = UUID.fastUUID().toString(true);

		CaptchaImage captchaImage = new CaptchaImage();
		captchaImage.setImg(base64Img);
		captchaImage.setUuid(uuid);

		cacheService.set(AppConstant.CACHE_CAPTCHA_KEY_PREFIX + uuid, meta.getImageNumber(), GlobalProperties.CAPTCHA_TIMEOUT);

		return captchaImage;

	}

	private CaptchaImageMeta drawCaptchaImage() {
		CaptchaImageMeta meta = new CaptchaImageMeta();
		System.setProperty("java.awt.headless", "true");
		// 设置图片大小
		int width = 100, height = 40;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		Random random = new Random();
		// 设定边框
		g.fillRect(1, 1, width, height);
		g.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 16));
		g.setColor(getRandColor(111, 133, random));
		// 产生随机线
		for (int i = 0; i < 11; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(13);
			int yl = random.nextInt(15);
			g.drawLine(x, y, x + xl, y + yl);
		}
		// 产生随机点
		g.setColor(getRandColor(130, 150, random));
		// 产生4个随机数
		StringBuilder sRand = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			g.setFont(getsFont());
			g.setColor(new Color(random.nextInt(101), random.nextInt(111), random.nextInt(121)));
			String rand = getRandomString(random.nextInt(10));
			sRand.append(rand);
			g.translate(random.nextInt(3), random.nextInt(3));
			g.drawString(rand, 24 * i + 8, 26);
		}
		meta.setImage(image);
		meta.setImageNumber(sRand.toString());
		g.dispose();
		return meta;
	}

	private String getRandomString(int num) {
		String randString = "0123456789";
		return String.valueOf(randString.charAt(num));
	}

}
