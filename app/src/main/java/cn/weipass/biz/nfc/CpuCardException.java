package cn.weipass.biz.nfc;

import java.io.IOException;

public class CpuCardException extends IOException {

	private static final long serialVersionUID = 296723882602230597L;
	private int code;

	// 63 CX 认证失败，X 为剩余尝试次数
	// 65 81 写 FLASH/EEPROM 失败
	// 67 00 数据长度错误
	// 69 81 文件类型不匹配
	// 69 82 安全状态不满足
	// 69 83 密钥已锁死
	// 69 85 使用条件不满足
	// 69 88 安全报文数据项（MAC）不正确
	// 6A 80 数据域参数不正确
	// 6A 81 功能不支持/无 MF/目录已锁定
	// 6A 82 没有找到文件
	// 6A 83 没有找到记录/密钥已存在
	// 6A 84 没有足够的空间
	// 6A 86 P1，P2 参数不正确
	// 6B 00 参数错误（偏移地址超出了 EF 文件长度）
	// 6D 00 不正确的 INS
	// 6E 00 不正确的 CLA
	// 6F 00 未定义的错误
	// 93 02 MAC 错误
	// 93 03 线路保护密钥锁死
	// 94 01 金额不足
	// 94 03 密钥未找到
	// 94 06 所需的 MAC 不可用
	// 94 20 生成密钥对失败

	private static String getMessage(int code) {
		String r = "错误码0x" + Integer.toHexString(code) + ",";
		String ex = "未知错误.";
		switch (code) {
		case 8: {
			ex = "密钥长度错误.";
			break;
		}
		case 0x6400:{
			ex="状态标志位没有变";
			break;
		}
		case 0x6581: {
			ex = "写 FLASH/EEPROM 失败";
			break;
		}
		case 0x6600: {
			ex = "接收通讯超时";
			break;
		}
		case 0x6601: {
			ex = "奇偶校验出错";
			break;
		}
		case 0x6602: {
			ex = "校验和不对";
			break;
		}
		case 0x6700: {
			ex = "长度错误";
			break;
		}
		case 0x6882:{
			ex = "不支持安全报文";
			break;
		}
		case 0x6981: {
			ex = "文件类型不匹配";
			break;
		}
		case 0x6982: {
			ex = "密钥使用权限不满足";
			break;
		}
		case 0x6983: {
			ex = "外部认证密钥锁死";
			break;
		}
		case 0x6984: {
			ex = "随机数无效，引用的数据无效";
			break;
		}
		case 0x6985: {
			ex = "使用条件不满足";
			break;
		}
		case 0x6986: {
			ex = "不满足命令执行条件（不允许的命令，INS有错）";
			break;
		}
		case 0x6987: {
			ex = "MAC丢失";
			break;
		}
		case 0x6988: {
			ex = "安全报文数据项（MAC）不正确";
			break;
		}
		case 0x6A80: {
			ex = "数据域参数不正确";
			break;
		}
		case 0x6A81: {
			ex = "功能不支持/无 MF/目录已锁定";
			break;
		}
		case 0x6A82: {
			ex = "没有找到文件";
			break;
		}
		case 0x6A83: {
			ex = "没有找到记录/密钥已存在";
			break;
		}
		case 0x6A84: {
			ex = "没有足够的空间";
			break;
		}
		case 0x6A86: {
			ex = "P1，P2 参数不正确";
			break;
		}
		case 0x6A88:{
			ex="引用数据未找到";
			break;
		}
		case 0x6B00: {
			ex = "参数错误（偏移地址超出了 EF 文件长度）";
			break;
		}
		case 0x6D00: {
			ex = "不正确的 INS";
			break;
		}
		case 0x6E00: {
			ex = "不正确的 CLA";
			break;
		}
		case 0x6F00: {
			ex = "未定义的错误";
			break;
		}
		case 0x9302: {
			ex = " MAC 错误";
			break;
		}
		case 0x9303: {
			ex = "线路保护密钥锁死";
			break;
		}
		case 0x9401: {
			ex = "金额不足";
			break;
		}
		case 0x9403: {
			ex = "密钥未找到";
			break;
		}
		case 0x9406: {
			ex = "所需的 MAC 不可用";
			break;
		}
		case 0x9420: {
			ex = "生成密钥对失败";
			break;
		}
		default: {
			if ((code >> 4) == 0x63C) {
				ex = "认证失败，剩余尝试次数:" + (code & 0xF);
			}
			break;
		}
		}
		return r + ex;
	}

	public CpuCardException(int code) {
		super(getMessage(code));
		this.code = code;
	}

	public CpuCardException(String msg) {
		super(msg);
	}

	public int getCode() {
		return code;
	}

}
