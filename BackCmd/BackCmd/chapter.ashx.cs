using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace BackCmd
{
    /// <summary>
    /// CHAPTER 的摘要说明
    /// </summary>
    public class CHAPTER : IHttpHandler
    {

        public void ProcessRequest(HttpContext context)
        {
            context.Response.ContentType = "text/plain";
            string userEmail = context.Request.QueryString["useremail"].ToString();
            //string userEmail = "1772348485@qq.com";
            string json = "{ \"verCode\":\"";
            json += Verfy.SendCaptch(userEmail) + "\"}";
            context.Response.Write(json);
        }

        public bool IsReusable
        {
            get
            {
                return false;
            }
        }
    }
}