package survivalgame.server.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import survivalgame.server.Server;
import survivalgame.server.network.packet.PacketRegistry;

import java.io.IOException;

public class NetworkManager {

    private final Server server;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Channel channel;

    public NetworkManager(Server server) {
        this.server = server;
    }

    public void start() throws IOException, InterruptedException {
        // Server part
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ClientInitializer(server));

        channel = b.bind(server.getConfig().server.port).sync().channel();

        Server.log.info("Server started on port " + server.getConfig().server.port + ".");
    }

    public boolean shutdown() {
        if (channel == null) {
            return false;
        }

        try {
            channel.close().sync();
            return true;
        } catch (InterruptedException ex) {
            return false;
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private static class ClientInitializer extends ChannelInitializer<SocketChannel> {

        private final Server server;

        public ClientInitializer(Server server) {
            this.server = server;
        }

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            try {
                ch.config().setOption(ChannelOption.IP_TOS, 0x18);
//                ch.config().setOption(ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, 32 * 1024);
//                ch.config().setOption(ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, 8 * 1024);
            } catch (ChannelException ex) {
                // IP_TOS not supported by platform, ignore
            }
            ch.config().setAllocator(PooledByteBufAllocator.DEFAULT);
            
            PacketRegistry r = new PacketRegistry();

            ch.pipeline().addLast(new HttpServerCodec());
            ch.pipeline().addLast(new HttpObjectAggregator(65536));
            ch.pipeline().addLast(new WebSocketHandler());
            ch.pipeline().addLast(new PacketDecoder(r));
            ch.pipeline().addLast(new PacketEncoder(r));
            ch.pipeline().addLast(new ClientHandler(server));
        }
    }
}
