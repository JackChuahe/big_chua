using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using MySql.Data.MySqlClient;

namespace BackCmd
{
    /// <summary>
    /// Activie 的摘要说明
    /// 激活
    /// </summary>
    public class Activie : IHttpHandler
    {
        static String connetStr = "server=localhost;User Id=root;password=root;Database=android";
        public void ProcessRequest(HttpContext context)
        {
            context.Response.ContentType = "text/plain";
            string useremail = context.Request.Form["useremail"];
            bool isActive = activity (useremail);
            if (isActive)
            {
                context.Response.Write("1");
            }
            else
            {
                context.Response.Write("0");
            }

        }

        private bool activity(string ue){
            //连接数据库进行update 操作
            MySqlConnection conn = new MySqlConnection(connetStr);
            conn.Open();

            String sql = "update user set state = 1 where useremail = \"" + ue + "\""; ;
            MySqlCommand comand = new MySqlCommand(sql, conn);
            if (comand.ExecuteNonQuery() == 1)
            {
                conn.Close();
                //插更新成功！
                return true;
            }
            conn.Close();
            return false;
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