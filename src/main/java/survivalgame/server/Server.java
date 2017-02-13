package survivalgame.server;

import java.util.logging.Logger;

import survivalgame.config.Config;
import survivalgame.server.network.NetworkManager;

public class Server {
    public static final Logger log = Logger.getGlobal();
    private Config mConfig;
    private NetworkManager mNetworkManager = new NetworkManager(this);

    public static void main(String[] args) throws Exception {
        new Server().start();
    }
    
    private Server() {}
    
    public void start() throws Exception {
        mNetworkManager.start();
    }
    
    public Config getConfig() {
        return mConfig;
    }
    
    public NetworkManager getNetworkManager() {
        return mNetworkManager;
    }

}
