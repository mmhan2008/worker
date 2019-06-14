package com.cnstock;

import com.cnstock.utils.HttpUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WorkerApplicationTests {

	@Test
	public void contextLoads() {
	}
	public static void main(String []args){
		System.out.println(123);


		String text[] = HttpUtil.httpPost("http://172.20.3.233:5000/gethtml?url="+"https://www.mct.gov.cn/whzx/whyw/","",null);
//		for(int i=0;i<10;i++){
//			TestRunnable t =new TestRunnable();
//			new Thread(t).start();
//			new Thread(t).start();
//			new Thread(t).start();
//			new Thread(t).start();
//		}
//		System.out.println(text);
//		System.out.println(text.hashCode());
//		text = text+" ";
//		System.out.println(text.hashCode());
	}
	static class TestRunnable implements Runnable{

		@Override
		public void run() {

			System.out.println(Thread.currentThread().getName()+"执行Run中");
			System.out.println(Thread.currentThread().getName()+"执行完毕");
		}
	}
}

