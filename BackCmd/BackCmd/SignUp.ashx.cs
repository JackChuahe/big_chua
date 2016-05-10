using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using MySql.Data.MySqlClient;

namespace BackCmd
{
    /// <summary>
    /// SignUp 的摘要说明
    /// </summary>
    public class SignUp : IHttpHandler
    {
        static String connetStr = "server=localhost;User Id=root;password=root;Database=android";
        public void ProcessRequest(HttpContext context)
        {
            context.Response.ContentType = "text/plain";
            string userEmail = context.Request.Form["useremail"];
            string pwd = context.Request.Form["pwd"];
            string username = context.Request.Form["username"];
            if (writeToDB(userEmail,username,pwd))
            {
                context.Response.Write("1");
            }
            else
            {
                context.Response.Write("0");
            }
        }


        private bool writeToDB(string userEmail, string username, string pwd)
        {
            MySqlConnection conn = new MySqlConnection(connetStr);
            conn.Open();

            String sql = "select * from user  where useremail = \""+userEmail+"\"";
            MySqlCommand comand = new MySqlCommand(sql,conn);
            MySqlDataReader reader = comand.ExecuteReader();
            if (reader.Read())
            {
                reader.Close();
                conn.Close();
                return false;
            }
            reader.Close();

            sql = "insert into user(useremail,username,pwd,state) values(\"" + userEmail + "\",\"" + username + "\",password('" + pwd + "'),1)";
            comand = new MySqlCommand(sql, conn);
            if (comand.ExecuteNonQuery() == 1)
            {
                conn.Close();
                //插入成功！
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