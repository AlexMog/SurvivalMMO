package survivalgame.server.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import survivalgame.server.Server;
import survivalgame.server.network.packet.Packet;
import survivalgame.server.world.Player;

import java.util.logging.Level;

public class ClientHandler extends SimpleChannelInboundHandler<Packet> {

    private final Server server;
    private Player player;

    public ClientHandler(Server server) {
        this.server = server;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.player = new Player(ctx.channel());
        // TODO: Players list
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // TODO: Remove from players list
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet packet) throws Exception {
        player.getConnection().handle(packet);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Server.log.log(Level.SEVERE, "Encountered exception in pipeline for client at " + ctx.channel().remoteAddress() + "; disconnecting client.", cause);
        ctx.channel().close();
    }
}
