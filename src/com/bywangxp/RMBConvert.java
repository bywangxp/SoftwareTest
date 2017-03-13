package com.bywangxp;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RMBConvert {
	static String[] money = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
	static String[] money1={"整", "拾", "佰", "仟" };
	static String[] money2={"元","万", "亿"};
	static String[] money3={"角","分"};
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println("请输入金额(整数部分不超过12位，小数部分最多两位)：");
			String next = scanner.next();
			//判断有.
			if(!next.contains(".")){
				next+=".00";
			}
			String[] part = next.split("\\.");
			String temp=part[0];
			//去前导0
			part[0]=removeZero(part[0]);
			Pattern pattern =null;
			Matcher matcher=null;
			if(part[0].length()==0){
				pattern=Pattern.compile("^\\d{0,2}$");
				matcher = pattern.matcher(part[1]);
			}else{
				//前导0
				pattern=Pattern.compile("^\\d{0,12}\\.{0,1}\\d{1,2}$");
				matcher = pattern.matcher(next);
			}
			if(!matcher.find()){
				System.out.println("输入有错误，请重新输入");
				continue;
			}
			String part1Convert = part1Convert(part[0]);//整数部分 323
//			System.out.println("整数部分："+part1Convert);
			String part2Convert = part2Convert(part[1]);//小数部分
			char[] result = part[0].toCharArray();
			StringBuffer sb = new StringBuffer();
			System.out.println("人民币"+ part1Convert+part2Convert);
		}

	}
	public static String removeZero(String str){
		String temp=str;
		for (int i = 0; i < str.length(); i++) {
			if('0'==str.charAt(i)){
				temp=str.substring(i+1, str.length());
			}else{
				return temp;
			}
		}	
		return temp;
	}
	private static String part2Convert(String part) {
		if(part.length()==0){
			return "";
			
		}
		//需要将小数截取成两位，防止是“0” “”或多于2两位小数
		if(part.equals("00")){
			return "整";
		}
		
		if(part.length()==1){
			part+="0";
		}
		//.12  1角2分   .01  0一分
		char[] str = part.toCharArray();
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<2;++i){
			sb.append(money[(str[i]-'0')]);
			if(i==0 && str[0]=='0'){
				sb.append(money3[i]);
				continue;
			}
			sb.append(money3[i]);
		}
		return sb.toString(); 
				
		
		
	}

	public static String part1Convert(String part) {//整数部分转变
		if(part.length()==0){
			return "零元";
			
		}
		char[] str = part.toCharArray();
		int length=str.length;
		//1.四等分
		int parlength=length/4;// 11/4=2  切割成四份完整的部分
		int parlength2=length%4;// 11%4=3; 高位不足四位的个数
		int flag=1;//设置标记位置，记录高位不足4位的部分进入循环
		StringBuffer sb = new StringBuffer();
		if(parlength2!=0){//如果高四位，不足四位，段数+1；
			parlength++;
			flag=0;
		}
		int k=0;
		int lastZero=1;//设置标志位，上一位末尾有没有添加0，如果添加了则本段第一个0，也不要加上去，1表示上一段不是0,但是要设置上一位的标志flag=0
		for (int i = parlength; i >0; i--) {//总共多少段 12/4==3
			int j=3;
			if (flag==0 ){//进入高位部分
				j=parlength2-1;
				flag=1;
			}
			//此处开始的是每段（4位1段）
			int zero=0;//设置标识位，这一段都是0，为零过滤掉全是0掉单位
			int tag=1;//设置标志位，表示出现上一位是0位,1表示上一位不是0
			for(;j>=0;--j){//正常是从4开始 4 3 2 
				int num=str[k++]-'0';
			//	String temp=null;
                if(num==0){
            	    if(tag==0){//当前为是0，上一位也是0
            	     	//temp=null;//temp表示上一位是0时的单位， 因为上一次也是0，则此处不需要记录上一位单位
            	     	if(j==0){
            	     		//当前是0，且上一次也是0
            	     		
            	     		sb.replace(sb.length()-1,sb.length(), "");//到达最后一位，最后一位也是0，将之前加的0，去掉
            	     				//此处还要继续判断上一位是不是0
            	     		if(zero==1||i==1){//如果当前段不是全0，则加上单位,或者是到达整数末尾
            	     			sb.append(money2[i-1]);
            	     		}
            	     		if(i!=1){
            	     			sb.append("零");
            	     		}
            	     		lastZero=0;
//            	     		if(lastZero!=0){//当本段全0，且上一段末尾没加0，则添加0
//	        	     			sb.append("零");
//	        	     			lastZero=0;
//	        	     		}
            	     		
            	     	}
            	     	continue;
            	    }
            	    //case 上一位不是0，本位是0
            	    //该位置可能是本段第一个位置，要判断要不要加0
            	
            	    sb.append("零");
            	    if(j==3&&lastZero==0){
            	    	sb.replace(sb.length()-1,sb.length(), "");
            	    }
            	    tag=0;
                 //	temp=money[num];//当前位应该加上的单位
                 	//至于这次出现的0后面要不要加单位，需要通过下次来判断。所以记录上一次的单位
                 	//如果下一次不是0则加上单位,是0则忽略
                 	
                	if(j==0){
                		//到达末尾，当前是0，之前不是0，也要则删去0，只有在整数最后一位是0才去掉
                		sb.replace(sb.length()-1,sb.length(), "");//到达最后一位，最后一位也是0，将之前加的0，去掉
//                		if(i==1){//如果是元则不去掉0,或者是末尾0，先去掉，后面加上
//                			System.out.println("qudiao");
//        	     		}
        	     		sb.append(money2[i-1]);
        	     		sb.append("零");
        	     	    lastZero=0;//设置标记上一次是0
        	     	}
                 	continue;
				}
                zero=1;//表示这一段出现了非0的情况
                //下面是当前位不是0,上一位是0的情况
                if(tag==0){//不是0
	               tag=1;
                }
				sb.append(money[num]); //3234     4  
				if(j==0){//此处增加每段最后一位
					sb.append(money2[i-1]);
					lastZero=1;//上一段末尾没加0
					continue;
				}
				sb.append(money1[j]);//此处增加每段前3位
			}
			
			
		}
		//去掉最后一位多余的0，这个0，是高段位，余下段下段，要删掉
		char charAt = sb.charAt(sb.length()-2);
		if(charAt=='零'){
			System.out.println("delete");
			sb.deleteCharAt(sb.length()-2);
		}
		return sb.toString();
	}
	

}