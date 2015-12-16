package org.cj.comm;

import android.annotation.SuppressLint;

public class StringUtil
{
	/**
	 * 特殊字符转换
	 * 
	 * @param value
	 * @return
	 */
	public static String filter(String value)
	{
		if (value == null) return null;
		char content[] = new char[value.length()];
		value.getChars(0, value.length(), content, 0);
		StringBuffer result = new StringBuffer(content.length + 50);
		for ( int i = 0; i < content.length; i++)
			switch (content[i])
			{
				case 60: // '<'
					result.append("&lt;");
					break;
				case 62: // '>'
					result.append("&gt;");
					break;
				case 38: // '&'
					result.append("&amp;");
					break;
				case 34: // '"'
					result.append("&quot;");
					break;
				case 39: // '\''
					result.append("&#39;");
					break;
				default:
					result.append(content[i]);
					break;
			}
		return result.toString();
	}
	static final int	GB_SP_DIFF	    = 160;
	// 存放国标一级汉字不同读音的起始区位码
	static final int[]	secPosValueList	= { 1601, 1637, 1833, 2078, 2274, 2302,
	        2433, 2594, 2787, 3106, 3212, 3472, 3635, 3722, 3730, 3858, 4027,
	        4086, 4390, 4558, 4684, 4925, 5249, 5600 };
	// 存放国标一级汉字不同读音的起始区位码对应读音
	static final char[]	firstLetter	    = { 'a', 'b', 'c', 'd', 'e', 'f', 'g',
	        'h', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'w',
	        'x', 'y', 'z'	            };

	// 获取一个字符串的拼音码
	@SuppressLint("DefaultLocale")
	public static String getFirstLetter(String oriStr)
	{
		String str = oriStr.toLowerCase();
		StringBuffer buffer = new StringBuffer();
		char ch;
		char[] temp;
		for ( int i = 0; i < str.length(); i++)
		{ // 依次处理str中每个字符
			ch = str.charAt(i);
			temp = new char[] { ch };
			byte[] uniCode = new String(temp).getBytes();
			if (uniCode[0] < 128 && uniCode[0] > 0)
			{ // 非汉字
				buffer.append(temp);
			} else
			{
				buffer.append(convert(uniCode));
			}
		}
		return buffer.toString();
	}

	/**
	 * 获取一个汉字的拼音首字母。 GB码两个字节分别减去160，转换成10进制码组合就可以得到区位码
	 * 例如汉字“你”的GB码是0xC4/0xE3，分别减去0xA0（160）就是0x24/0x43
	 * 0x24转成10进制就是36，0x43是67，那么它的区位码就是3667，在对照表中读音为‘n’
	 */
	static char convert(byte[] bytes)
	{
		char result = '-';
		int secPosValue = 0;
		int i;
		for ( i = 0; i < bytes.length; i++)
		{
			bytes[i] -= GB_SP_DIFF;
		}
		secPosValue = bytes[0] * 100 + bytes[1];
		for ( i = 0; i < 23; i++)
		{
			if (secPosValue >= secPosValueList[i]
			        && secPosValue < secPosValueList[i + 1])
			{
				result = firstLetter[i];
				break;
			}
		}
		return result;
	}

		//字母Z使用了两个标签，这里有27个值
		//i, u, v都不做声母, 跟随前面的字母
		private static char[]	chartable	= { '啊', '芭', '擦', '搭', '蛾', '发', '噶', '哈',
		                                   '哈', '击', '喀', '垃', '妈', '拿', '哦',
		                                   '啪', '期', '然', '撒', '塌', '塌', '塌',
		                                   '挖', '昔', '压', '匝', '座' };
		private static char[]	alphatable	= { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
		                                   'I', 'J', 'K', 'L', 'M', 'N', 'O',
		                                   'P', 'Q', 'R', 'S', 'T', 'U', 'V',
		                                   'W', 'X', 'Y', 'Z' };
		private static int[]	table		= new int[27];
		//初始化
		{
			for ( int i = 0; i < 27; ++i)
			{
				table[i] = gbValue(chartable[i]);
			}
		}

		//主函数,输入字符,得到他的声母,
		//英文字母返回对应的大写字母
		//其他非简体汉字返回 '0'
		static char Char2Alpha(char ch)
		{
			if (ch >= 'a' && ch <= 'z') return (char) (ch - 'a' + 'A');
			if (ch >= 'A' && ch <= 'Z') return ch;
			int gb = gbValue(ch);
			if (gb < table[0]) return '0';
			int i;
			for ( i = 0; i < 26; ++i)
			{
				if (match(i, gb)) break;
			}
			if (i >= 26) return '0';
			else
				return alphatable[i];
		}

		//根据一个包含汉字的字符串返回一个汉字拼音首字母的字符串
		public static String String2Alpha(String SourceStr)
		{
			String Result = "";
			int StrLength = SourceStr.length();
			int i;
			try
			{
				for ( i = 0; i < StrLength; i++)
				{
					Result += Char2Alpha(SourceStr.charAt(i));
				}
			} catch (Exception e)
			{
				Result = "";
			}
			return Result;
		}

		private static boolean match(int i, int gb)
		{
			if (gb < table[i]) return false;
			int j = i + 1;
			//字母Z使用了两个标签
			while (j < 26 && (table[j] == table[i]))
				++j;
			if (j == 26) return gb <= table[j];
			else
				return gb < table[j];
		}

		//取出汉字的编码
		private static int gbValue(char ch)
		{
			String str = new String();
			str += ch;
			try
			{
				byte[] bytes = str.getBytes("GB2312");
				if (bytes.length < 2) return 0;
				return (bytes[0] << 8 & 0xff00) + (bytes[1] & 0xff);
			} catch (Exception e)
			{
				return 0;
			}
	}
}
