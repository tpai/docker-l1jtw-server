using System;
using System.Collections.Generic;
using System.Linq;
using System.Windows.Forms;

namespace L1J_TW_Ip_Convertor_for_3._3C
{
    static class Program
    {
        /// <summary>
        /// 應用程式的主要進入點。
        /// </summary>
        [STAThread]
        static void Main()
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Application.Run(new Form1());
        }
    }
}
