/**
 * Created by 14334 on 2017/4/28.
 */
package javaclass2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class ConcurrentTest {

    private static int thread_num =30;// 200;
    private static int client_num = 30;// 460;


    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        ExecutorService exec = Executors.newCachedThreadPool();
        //thread_num个线程可以同时访问
        final Semaphore semp = new Semaphore(thread_num);
        //模拟2000个客户端访问
        for (int index = 0; index < client_num; index++) {
            final int NO = index;
            exec.execute(new TaskThread(semp, NO));
        }
        long timeSpend = (System.currentTimeMillis()-start)/1000;
        System.out.println( "花费1: "+timeSpend +"秒" );
        //退出线程池
        exec.shutdown();
        timeSpend = System.currentTimeMillis()-start;
        System.out.println( "花费2: "+timeSpend +"秒" );
        //long end = (System.currentTimeMillis()-start)/1000;//当前时间离当天0点的毫秒数

    }


    static class TaskThread implements Runnable {
        Semaphore semp;
        int NO;
        public TaskThread(Semaphore semp, int NO) {
            this.semp = semp;
            this.NO = NO;
        }
        @Override
        public void run() {
            try {
                //获取许可
                semp.acquire();
                System.out.println("Thread:" + NO);
                String host = "http://192.168.2.10:8080/revitbus/revit/list.html?";
                String para = "&type=0&system=1&year=2016";

                System.out.println(host + para);
                URL url = new URL(host);// 此处填写供测试的url
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Proxy-Connection", "Keep-Alive");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                PrintWriter out = new PrintWriter(connection.getOutputStream());
                out.print(para);
                out.flush();
                out.close();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                String result = "";
                while ((line = in.readLine()) != null) {
                    result += line;
                }

                System.out.println("第：" + NO + " 个");
                semp.release();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        };
    }


}