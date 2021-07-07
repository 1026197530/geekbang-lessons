package com.salesmanager.shop.tags.HelloTag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.io.StringWriter;

public class HelloTag extends SimpleTagSupport {
    private String message;

    public void setMessage(String msg) {
        this.message = msg;
    }

    StringWriter sw = new StringWriter();

    @Override
    public void doTag() throws JspException, IOException {
//        JspWriter out = getJspContext().getOut();
//        out.println("Hello Custom Tag!");

        if (message != null) {
            /* 从属性中使用消息 */
            JspWriter out = getJspContext().getOut();
            out.println( message );
        }
        else {
            /* 从内容体中使用消息 */
            getJspBody().invoke(sw);
            getJspContext().getOut().println(sw.toString());
        }
    }

}
