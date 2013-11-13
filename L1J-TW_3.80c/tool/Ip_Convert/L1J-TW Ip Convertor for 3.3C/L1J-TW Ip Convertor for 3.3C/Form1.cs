using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace L1J_TW_Ip_Convertor_for_3._3C
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            /**
             * @使用者輸入的 xxx.xxx.xxx.xxx
             **/
            int input1 = System.Convert.ToInt16(textBox1.Text);
            int input2 = System.Convert.ToInt16(textBox2.Text);
            int input3 = System.Convert.ToInt16(textBox3.Text);
            int input4 = System.Convert.ToInt16(textBox4.Text);

            /**運算部分*/
            long result = (System.Convert.ToInt64(Math.Pow(256,3)) * input1) + (System.Convert.ToInt32(Math.Pow(256,2)) * input2)
                       + (System.Convert.ToInt32(Math.Pow(256,1)) * input3) + (System.Convert.ToInt32(Math.Pow(256,0)) * input4);

            /**輸出部分*/
            richTextBox1.Text = result.ToString();

        }

        private void richTextBox1_TextChanged(object sender, EventArgs e)
        {

        }

        private void Form1_Load(object sender, EventArgs e)
        {

        }
    }
}
