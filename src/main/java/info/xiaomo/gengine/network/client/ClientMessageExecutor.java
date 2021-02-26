package info.xiaomo.gengine.network.client;

import java.util.Map;
import info.xiaomo.gengine.network.IMessagePool;
import info.xiaomo.gengine.network.INetworkConsumer;
import info.xiaomo.gengine.network.INetworkEventListener;
import info.xiaomo.gengine.network.MsgPack;
import info.xiaomo.gengine.network.handler.MessageExecutor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class ClientMessageExecutor extends MessageExecutor {

    protected Map<Short, ClientFuture<MsgPack>> futureMap;

    protected IMessagePool pool;

    protected boolean idleCheck;

    public ClientMessageExecutor(
            INetworkConsumer consumer,
            INetworkEventListener listener,
            IMessagePool pool,
            Map<Short, ClientFuture<MsgPack>> futureMap,
            boolean idleCheck) {
        super(consumer, listener, pool);
        this.futureMap = futureMap;
        this.idleCheck = idleCheck;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        MsgPack m = (MsgPack) msg;
        ClientFuture<MsgPack> f = futureMap.get(m.getSequence());
        if (f != null) {
            if (!f.isCancelled()) {
                f.result(m);
            }
        } else {
            super.channelRead(ctx, msg);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            IdleState state = idleStateEvent.state();
            if (state == IdleState.READER_IDLE) {
                this.listener.idle(ctx, state);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
