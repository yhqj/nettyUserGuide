package example1;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * 处理服务端 channel.
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {// (1)
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
//        // 默默地丢弃收到的数据
//        ((ByteBuf) msg).release(); // (3)
//    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;
        try {
            while (in.isReadable()) { // (1)
                System.out.print((char) in.readByte());
                System.out.flush();
            }
        } finally {
            ReferenceCountUtil.release(msg); // (2)
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }
    //ByteBuf 是一个引用计数对象，这个对象必须显示地调用
    // release() 方法来释放。请记住处理器的职责是释放所有传递到处理器的引用计数对象。
    // 通常，channelRead() 方法的实现就像下面的这段代码

//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) {
//        try {
//            // Do something with msg
//        } finally {
//            ReferenceCountUtil.release(msg);
//        }
//    }


}
