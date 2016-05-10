using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using MySql.Data.MySqlClient;

namespace BackCmd
{
    /// <summary>
    /// Login 的摘要说明
    /// </summary>
    public class Login : IHttpHandler
    {
        static String connetStr = "server=localhost;User Id=root;password=root;Database=android";
        private string username = null;
        public void ProcessRequest(HttpContext context)
        {
            context.Response.ContentType = "text/plain";
            string userEmail = context.Request.Form["useremail"];
            string pwd = context.Request.Form["pwd"];
            //String userEmail = "601825672";
            //String pwd = "c601825672";
            bool isRight = connetDB(userEmail, pwd);
            string json = "{\"isOk\":\"";
            if (isRight)
            {
                json += "1\",\"username\":\"" + username + "\"}";
                 context.Response.Write(json);
            }
            else
            {
                json += "0\"}";
                context.Response.Write(json);
            }

        }

        private bool connetDB(String ue,String pwd){
            MySqlConnection conn = new MySqlConnection(connetStr);
            conn.Open();
            String sql = "select username from user where useremail = '" + ue + "' and pwd = password('" + pwd + "') and state  = '1'"; 
            MySqlCommand comand = new MySqlCommand(sql,conn);
            MySqlDataReader reader = comand.ExecuteReader();
            if (reader.Read())
            {
                username = reader.GetString(0);
                reader.Close();
                conn.Close();
                return true;
            }
            else
            {
                reader.Close();
                conn.Close();
                return false;
            }


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