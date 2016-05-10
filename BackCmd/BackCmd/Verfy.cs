using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Net.Mail;
using System.Net;
using System.IO;
using System.Text;

namespace BackCmd
{
    public class Verfy
    {
        public static string  SendCaptch(String userEmail)
        {
            String vCode = System.Environment.TickCount.ToString();
            sendEmail(userEmail,vCode);
            return vCode;

        }

        /// <summary>
        /// 发送邮箱验证信息
        /// </summary>
        /// <param name="userEmail"></param>
        private static  bool sendEmail(string userEmail ,String content)
        {
            try
            {
                MailMessage msg = new MailMessage();
                msg.From = new MailAddress("601825672@qq.com");   //发件人的邮箱地址
                msg.Subject = "BigChua Register Account Verify";  //邮件主题
                msg.Body = getBody(userEmail,content);//邮件正文
                msg.To.Add(userEmail);
                msg.IsBodyHtml = true;  //邮件正文是否支持html的值
                SmtpClient sc = new SmtpClient();
                sc.Host = "smtp.qq.com";//smtp.qq.com
                sc.Port = 25;
                NetworkCredential nc = new NetworkCredential("601825672", "caihe601825672");  //验证凭据 1607977350：是邮箱账号，********：是邮箱密码
                sc.Credentials = nc;
                sc.Send(msg);
                return true;
            }
            catch (Exception my)
            {
                // 错误的邮箱
                return false;
            }
        }

        /// <summary>
        /// 生成发送邮箱验证的html页面
        /// </summary>
        /// <param name="userEmail"></param>
        /// <returns></returns>
        private static string getBody(string userEmail,String content)
        {

            //string sPath = System.IO.Path.GetDirectoryName(context.Request.PhysicalPath);
            string sPath = System.Web.HttpContext.Current.Request.MapPath("/");
            //string path2 = System.Diagnostics.Process.GetCurrentProcess().MainModule.FileName;
            //string path3 = System.Environment.CurrentDirectory;
            string path = sPath + "Email.html";
            string href = "谢谢,计算机科学学院!";
            StreamReader sr = new StreamReader(path, Encoding.Default);
            String line;
            String body = "";
            while ((line = sr.ReadLine()) != null)
            {
                body += line;
            }
            sr.Close();
            body = body.Replace("NO1REPLACE", userEmail);
            body = body.Replace("haha", "Verify Code: "+content);
            return body;
        }
    }
}