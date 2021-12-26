package com.myf.wind.base.rpcdemo.rpc.transport.handler;

import com.myf.wind.base.rpcdemo.rpc.Dispatcher;
import com.myf.wind.base.rpcdemo.rpc.protocol.MyContent;
import com.myf.wind.base.rpcdemo.util.SerDerUtil;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author : wind-myf
 * @desc : 基于http协议处理
 */
public class MyHttpRpcHandler extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletInputStream inputStream = req.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(inputStream);
        try {
            MyContent myContent = (MyContent) ois.readObject();
            String interfaceName = myContent.getInterfaceName();
            String methodName = myContent.getMethodName();
            Object invoke = Dispatcher.getInstance().getInvoke(interfaceName);
            MyContent responseContent = SerDerUtil.getInvokeResult(myContent, methodName, invoke);

            ServletOutputStream outputStream = resp.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.writeObject(responseContent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
